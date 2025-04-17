package com.minewaku.trilog.specification;

import org.springframework.data.jpa.domain.Specification;
import com.minewaku.trilog.entity.Permission;

public class PermissionSpecification {
    
    public static Specification<Permission> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Permission> containsName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Permission> hasDescription(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("description"), description);
    }

    public static Specification<Permission> containsDescription(String description) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }
}
