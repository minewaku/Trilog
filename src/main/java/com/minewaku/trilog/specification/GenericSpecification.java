package com.minewaku.trilog.specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {
    private final Criteria criteria;
    private final Set<SingularAttribute<T, ?>> allowedFields;

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        return createPredicate(root, builder, criteria);
    }

    private Predicate createPredicate(Root<T> root, CriteriaBuilder builder, Criteria criteria) {
        SingularAttribute<T, ?> field = findField(criteria.getField());
        Object convertedValue = convertValue(criteria.getValue(), field.getJavaType());

        return switch (criteria.getOperation()) {
            case EQUALS -> builder.equal(
                root.get(criteria.getField()), 
                convertedValue
            );
            case CONTAINS -> builder.like(
                builder.lower(root.get(criteria.getField())), 
                "%" + convertedValue.toString().toLowerCase() + "%"
            );
            case STARTS_WITH -> builder.like(
                builder.lower(root.get(criteria.getField())), 
                convertedValue.toString().toLowerCase() + "%"
            );
            case ENDS_WITH -> builder.like(
                builder.lower(root.get(criteria.getField())), 
                "%" + convertedValue.toString().toLowerCase()
            );
            case GREATER_THAN -> builder.greaterThan(
                root.get(criteria.getField()), 
                (Comparable) convertedValue
            );
            case LESS_THAN -> builder.lessThan(
                root.get(criteria.getField()), 
                (Comparable) convertedValue
            );
        };
    }

    private SingularAttribute<T, ?> findField(String fieldName) {
        return allowedFields.stream()
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Field not found: " + fieldName));
    }

    private Object convertValue(String value, Class<?> targetType) {
        if (targetType.equals(String.class)) {
            return value;
        } else if (targetType.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (targetType.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (targetType.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (targetType.equals(LocalDate.class)) {
            return LocalDate.parse(value);
        } else if (targetType.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(value);
        }
        throw new IllegalArgumentException("Unsupported type: " + targetType);
    }
}
