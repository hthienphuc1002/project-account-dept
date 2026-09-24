package com.example.accountdept.service;

import com.example.accountdept.dto.*;
import com.example.accountdept.entity.DepartmentType;
import org.springframework.data.domain.*;

import java.time.Instant;

public interface DepartmentService {
    Page<DepartmentDTO> list(String search, DepartmentType type, Instant createdFrom, Instant createdTo, Pageable pageable);
    DepartmentDTO create(DepartmentCreateDTO dto);
    DepartmentDTO update(Long id, DepartmentUpdateDTO dto);
    void delete(Long id);
    DepartmentDTO get(Long id);
}
