package com.example.accountdept.config;

import com.example.accountdept.entity.Account;
import com.example.accountdept.entity.Department;
import com.example.accountdept.entity.DepartmentType;
import com.example.accountdept.entity.Role;
import com.example.accountdept.repository.AccountRepository;
import com.example.accountdept.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Tạo dữ liệu mẫu khi bảng còn trống (thay cho data.sql cũ, vốn sai tên cột và dùng hash giả).
 * Tài khoản mẫu: admin / 123456 (ADMIN), user2..user30 / 123456.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            departmentRepository.saveAll(List.of(
                    Department.builder().name("IT").type(DepartmentType.IT).build(),
                    Department.builder().name("HR").type(DepartmentType.HR).build(),
                    Department.builder().name("Finance").type(DepartmentType.FINANCE).build()));
        }
        if (accountRepository.count() > 0) return;

        List<Department> depts = departmentRepository.findAll();
        String hash = passwordEncoder.encode("123456");

        accountRepository.save(Account.builder()
                .username("admin").firstName("Admin").lastName("User")
                .email("admin@example.com").password(hash)
                .role(Role.ADMIN).department(depts.get(0)).build());

        for (int i = 2; i <= 30; i++) {
            accountRepository.save(Account.builder()
                    .username("user" + i).firstName("First" + i).lastName("Last" + i)
                    .email("user" + i + "@example.com").password(hash)
                    .role(i % 5 == 0 ? Role.MANAGER : Role.EMPLOYEE)
                    .department(depts.get(i % depts.size()))
                    .build());
        }
    }
}
