package com.example.accountdept.dto;

import com.example.accountdept.entity.DepartmentType;
import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class DepartmentUpdateDTO {
    @Size(max = 255)
    private String name;

    private DepartmentType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DepartmentType getType() {
		return type;
	}

	public void setType(DepartmentType type) {
		this.type = type;
	}
}