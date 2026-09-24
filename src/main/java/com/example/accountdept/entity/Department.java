package com.example.accountdept.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

@Entity @Table(name = "departments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DepartmentType type = DepartmentType.OTHER;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdDate = Instant.now();

    @OneToMany(mappedBy = "department")
    private List<Account> accounts;
}
