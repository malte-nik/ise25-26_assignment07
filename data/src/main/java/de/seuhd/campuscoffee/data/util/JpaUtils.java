package de.seuhd.campuscoffee.data.util;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.lang.reflect.Field;

/**
 * Utility class for JPA-related functionality.
 */
public class JpaUtils {
    /**
     * Extracts the table name from the entity's @Table annotation.
     */
    public static String extractTableNameFromEntity(Class<?> entityClass) {
        if (!entityClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException(String.format("%s is not annotated with @Table",
                    entityClass.getSimpleName())
            );
        }

        Table table = entityClass.getAnnotation(Table.class);
        String tableName = table.name();
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("@Table annotation must specify a table name.");
        }

        return tableName;
    }

    /**
     * Checks if a field has @Column(unique=true).
     */
    public static boolean isUniqueField(Field field) {
        if (!field.isAnnotationPresent(Column.class)) {
            return false;
        }
        Column column = field.getAnnotation(Column.class);
        return column.unique();
    }

    /**
     * Extracts the column name from the @Column annotation.
     */
    public static String extractColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null && !column.name().isEmpty()) {
            return column.name();
        }
        // fallback: use field name if no column name specified
        return field.getName();
    }
}
