package com.example.accountdept.service.impl;

import com.example.accountdept.dto.*;
import com.example.accountdept.entity.*;
import com.example.accountdept.exception.ApiException;
import com.example.accountdept.repository.*;
import com.example.accountdept.spec.AccountSpecifications;
import com.example.accountdept.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public Page<AccountDTO> list(String search, Role role, Long departmentId, Instant createdFrom, Instant createdTo, Pageable pageable) {
        Specification<Account> spec = Specification
                .where(AccountSpecifications.search(search))
                .and(AccountSpecifications.hasRole(role))
                .and(AccountSpecifications.inDepartment(departmentId))
                .and(AccountSpecifications.createdBetween(createdFrom, createdTo));
        Page<Account> page = accountRepository.findAll(spec, pageable);
        return page.map(this::toDTO);
    }

    @Transactional
    @Override
    public AccountDTO create(AccountCreateDTO dto) {
        if (accountRepository.existsByUsername(dto.getUsername())) throw new ApiException("USERNAME_EXISTS");
        if (accountRepository.existsByEmail(dto.getEmail())) throw new ApiException("EMAIL_EXISTS");
        com.example.accountdept.entity.Department dept = null;
        if (dto.getDepartmentId() != null) {
            dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ApiException("DEPARTMENT_NOT_FOUND"));
        }
        Account entity = Account.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole() == null ? Role.EMPLOYEE : dto.getRole())
                .department(dept)
                .active(dto.getActive() == null ? Boolean.TRUE : dto.getActive())
                .createdDate(Instant.now())
                .build();
        entity = accountRepository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    @Override
    public AccountDTO update(Long id, AccountUpdateDTO dto) {
        Account entity = accountRepository.findById(id).orElseThrow(() -> new ApiException("ACCOUNT_NOT_FOUND"));
        if (accountRepository.existsByEmailAndIdNot(dto.getEmail(), id)) throw new ApiException("EMAIL_EXISTS");
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        if (dto.getRole() != null) entity.setRole(dto.getRole());
        if (dto.getDepartmentId() != null) {
            entity.setDepartment(departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ApiException("DEPARTMENT_NOT_FOUND")));
        }
        if (dto.getActive() != null) entity.setActive(dto.getActive());
        return toDTO(entity);
    }

    @Transactional
    @Override
    public void deleteMany(List<Long> ids) {
        accountRepository.deleteAllById(ids);
    }

    @Override
    public AccountDTO get(Long id) {
        return accountRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new ApiException("ACCOUNT_NOT_FOUND"));
    }

    private AccountDTO toDTO(Account a) {
        AccountDTO dto = mapper.map(a, AccountDTO.class);
        if (a.getDepartment() != null) {
            dto.setDepartmentId(a.getDepartment().getId());
            dto.setDepartmentName(a.getDepartment().getName());
        }
        return dto;
    }
}