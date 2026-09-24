package com.example.accountdept.service.impl;

import com.example.accountdept.dto.*;
// CHỈ import Department từ entity, KHÔNG import từ dto
import com.example.accountdept.entity.Department;
import com.example.accountdept.entity.DepartmentType;
import com.example.accountdept.exception.ApiException;
import com.example.accountdept.repository.*;
import com.example.accountdept.spec.DepartmentSpecifications;
import com.example.accountdept.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor // Sửa lỗi NullPointerException cho các repository và mapper
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper mapper;

    @Override
    public Page<DepartmentDTO> list(String search, DepartmentType type, Instant createdFrom, Instant createdTo, Pageable pageable) {
        Specification<Department> spec = Specification
                .where(DepartmentSpecifications.search(search))
                .and(DepartmentSpecifications.typeIs(type))
                .and(DepartmentSpecifications.createdBetween(createdFrom, createdTo));
        
        Page<Department> page = departmentRepository.findAll(spec, pageable);
        return page.map(this::toDTO);
    }

    @Transactional
    @Override
    public DepartmentDTO create(DepartmentCreateDTO dto) {
        if (departmentRepository.existsByName(dto.getName())) {
            throw new ApiException("DEPARTMENT_EXISTS");
        }

        // Hết lỗi gạch đỏ dòng này nếu bạn đã xóa file dto/Department.java
        Department d = Department.builder()
                .name(dto.getName())
                .type(dto.getType()) 
                .createdDate(Instant.now())
                .build();
        
        d = departmentRepository.save(d);
        return toDTO(d);
    }

    @Transactional
    @Override
    public DepartmentDTO update(Long id, DepartmentUpdateDTO dto) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("DEPARTMENT_NOT_FOUND"));
        
        d.setName(dto.getName());
        if (dto.getType() != null) {
            d.setType(dto.getType());
        }
        
        return toDTO(d);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (accountRepository.existsByDepartmentId(id)) {
            throw new ApiException("DEPARTMENT_HAS_MEMBERS");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentDTO get(Long id) {
        Department d = departmentRepository.findById(id)
                .orElseThrow(() -> new ApiException("DEPARTMENT_NOT_FOUND"));
        return toDTO(d);
    }

    private DepartmentDTO toDTO(Department d) {
        DepartmentDTO dto = mapper.map(d, DepartmentDTO.class);
        
        long total = accountRepository.countByDepartmentId(d.getId());
        dto.setTotalMember(total);
        
        return dto;
    }
}