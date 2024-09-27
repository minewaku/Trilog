package com.minewaku.trilog.util.BuilderUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BuilderGenerator<T> {
    private Class<T> clazz;
    private T instance;
    private final Map<String, Consumer<Object>> setters = new HashMap<>();

    public BuilderGenerator(Class<T> clazz) {
        this.clazz = clazz;
        this.instance = createdInstance(clazz);
        generateBuiler();
    }

    private void generateBuiler() {
        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(FieldBuilder.class)) {
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                setters.put(fieldName, value -> {
                    try {
                        field.set(instance, fieldType.cast(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                        System.err.println("Unable to assign " + value + " to " + fieldName + " of type " + fieldType);
                    }
                });
            }
        }
    }

    private T createdInstance(Class<T> clazz) {
        try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();		
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return null;
    }

    public BuilderGenerator<T> set(String fieldName, Object value) {
        Consumer<Object> setter = setters.get(fieldName);
        if (setter != null) {
            setter.accept(value);
        } else {
            throw new IllegalArgumentException("No such field: " + fieldName);
        }
        return this;
    }

    public T build() {
        return instance;
    }
}
