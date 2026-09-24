package com.example.accountdept.spec;

import com.example.accountdept.entity.*;
import jakarta.persistence.criteria.JoinType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class DepartmentSpecifications {
    public static Specification<Department> search(String keyword) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(keyword)) return cb.conjunction();
            String like = "%" + keyword.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), like);
        };
    }

    public static Specification<Department> typeIs(DepartmentType type) {
        return (root, query, cb) -> type == null ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    public static Specification<Department> createdBetween(Instant from, Instant to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null) return cb.between(root.get("createdDate"), from, to);
            if (from != null) return cb.greaterThanOrEqualTo(root.get("createdDate"), from);
            return cb.lessThanOrEqualTo(root.get("createdDate"), to);
        };
    }
}
