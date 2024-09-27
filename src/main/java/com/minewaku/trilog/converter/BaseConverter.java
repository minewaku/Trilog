package com.minewaku.trilog.converter;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;

@Component
public abstract class BaseConverter<D, E> {

    private Class<D> dtoClass;
    private Class<E> entityClass;

    public BaseConverter(Class<D> dtoClass, Class<E> entityClass) {
        this.dtoClass =  dtoClass;   
        this.entityClass = entityClass;
    }

    public E dtoToEntity(D dto) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            convertFields(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public D entityToDto(E entity) {
        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();
            convertFields(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void convertFields(Object source, Object target) throws IllegalAccessException, InstantiationException {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        Field[] targetFields = target.getClass().getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            for (Field targetField : targetFields) {
                targetField.setAccessible(true);
                if (sourceField.getName().equals(targetField.getName())) {
                    if (sourceField.getType().equals(targetField.getType())) {
                        targetField.set(target, sourceField.get(source));
                    } else if (!sourceField.getType().isPrimitive() && !sourceField.getType().equals(String.class)) {
                        // Recursively convert nested fields
                        Object nestedSource = sourceField.get(source);
                        Object nestedTarget = targetField.getType().newInstance();
                        convertFields(nestedSource, nestedTarget);
                        targetField.set(target, nestedTarget);
                    } else {
                        targetField.set(target, sourceField.get(source));
                    }
                }
            }
        }
    }
}