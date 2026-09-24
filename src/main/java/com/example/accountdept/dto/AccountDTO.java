package com.example.accountdept.dto;

import com.example.accountdept.entity.Role;
import lombok.Data;
import java.time.Instant;

@Data
public class AccountDTO {
    private Long id;
    private String username;
	private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Long departmentId;
    private String departmentName;
    private Boolean active;
    private Instant createdDate;
}
