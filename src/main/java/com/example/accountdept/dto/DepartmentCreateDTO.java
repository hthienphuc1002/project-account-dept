package com.example.accountdept.dto;

import com.example.accountdept.entity.DepartmentType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentCreateDTO {
    @NotBlank
    private String name;
    
    private DepartmentType type = DepartmentType.OTHER;

    public String getName() {
        return name;
    }

    public DepartmentType getType() {                                                                     
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }
}