package com.example.accountdept.dto;

import com.example.accountdept.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AccountUpdateDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email @NotBlank
    private String email;
	private Role role;
    private Long departmentId;
    private Boolean active;
}
