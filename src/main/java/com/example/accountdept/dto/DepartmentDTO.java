package com.example.accountdept.dto;

import com.example.accountdept.entity.DepartmentType;
import lombok.Data;
import java.time.Instant;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private DepartmentType type;
    private Instant createdDate;
    private Long totalMember;
}
