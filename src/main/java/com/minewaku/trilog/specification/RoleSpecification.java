package com.minewaku.trilog.specification;

import org.springframework.data.jpa.domain.Specification;

import com.minewaku.trilog.entity.Role;

public class RoleSpecification {
    
    public static Specification<Role> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Role> containsName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Role> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("description"), description);
    }

    public static Specification<Role> containsDescription(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }
}
