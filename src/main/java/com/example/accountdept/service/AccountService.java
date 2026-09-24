package com.example.accountdept.service;

import com.example.accountdept.dto.*;
import com.example.accountdept.entity.Role;
import org.springframework.data.domain.*;

import java.time.Instant;
import java.util.List;

public interface AccountService {
    Page<AccountDTO> list(String search, Role role, Long departmentId, Instant createdFrom, Instant createdTo, Pageable pageable);
    AccountDTO create(AccountCreateDTO dto);
    AccountDTO update(Long id, AccountUpdateDTO dto);
    void deleteMany(List<Long> ids);
    AccountDTO get(Long id);
}
