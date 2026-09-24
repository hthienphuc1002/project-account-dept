package com.example.accountdept.spec;

import com.example.accountdept.entity.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

public class AccountSpecifications {
    public static Specification<Account> search(String keyword) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(keyword)) return cb.conjunction();
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("username")), like),
                cb.like(cb.lower(root.get("firstName")), like),
                cb.like(cb.lower(root.get("lastName")), like)
            );
        };
    }

    public static Specification<Account> hasRole(Role role) {
        return (root, query, cb) -> role == null ? cb.conjunction() : cb.equal(root.get("role"), role);
    }

    public static Specification<Account> inDepartment(Long departmentId) {
        return (root, query, cb) -> departmentId == null ? cb.conjunction()
                : cb.equal(root.get("department").get("id"), departmentId);
    }

    public static Specification<Account> createdBetween(Instant from, Instant to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null) return cb.between(root.get("createdDate"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("createdDate"), from);
            return cb.lessThanOrEqualTo(root.get("createdDate"), to);
        };
    }
}
