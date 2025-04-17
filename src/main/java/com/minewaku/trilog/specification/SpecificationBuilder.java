package com.minewaku.trilog.specification;

import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.minewaku.trilog.util.ErrorUtil;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class SpecificationBuilder<T> {
    @Autowired
    private ErrorUtil errorUtil;

    public Specification<T> buildSpecification(Map<String, String> params, Set<SingularAttribute<T, ?>> allowedFields) {
        return params.entrySet().stream()
                .filter(entry -> StringUtils.hasLength(entry.getValue()))
                .map(entry -> createCriteria(entry, allowedFields))
                .filter(criteria -> criteria != null)  // Filter out invalid criteria
                .map(criteria -> (Specification<T>) new GenericSpecification<T>(criteria, allowedFields))
                .reduce(Specification::and)
                .orElse(Specification.where(null));
    }

    private Criteria createCriteria(Map.Entry<String, String> entry, Set<SingularAttribute<T, ?>> allowedFields) {
        String[] keyParts = extractFieldAndOperator(entry.getKey());
        String field = keyParts[1];
        String value = entry.getValue();

        // Return null if field is not valid
        if (!isValidField(field, allowedFields)) {
            return null;
        }

        Operation operation = Operation.SYMBOL_MAP.get(keyParts[0]);
        if (operation == null) {
            operation = Operation.EQUALS; // Default to EQUALS if no operator found
        }

        return new Criteria(field, operation, value);
    }

    private String[] extractFieldAndOperator(String prefixKey) {
        String[] parts = prefixKey.contains("_")
                ? new String[]{
                        prefixKey.substring(0, prefixKey.indexOf("_")),
                        prefixKey.substring(prefixKey.indexOf("_") + 1)
                }
                : new String[]{"", prefixKey}; // Empty string will map to EQUALS
        return parts;
    }

    private boolean isValidField(String field, Set<SingularAttribute<T, ?>> allowedFields) {
        return allowedFields.stream()
                .anyMatch(allowedField -> allowedField.getName().equals(field));
    }
}