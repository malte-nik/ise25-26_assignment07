package de.seuhd.campuscoffee.data.constraints;

import de.seuhd.campuscoffee.data.util.JpaUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Extracts unique field constraints from JPA entity classes using reflection.
 * This component automatically discovers constraints by inspecting JPA annotations.
 * <p>
 * The extractor scans for:
 * <ul>
 *   <li>{@code @Column(unique=true)} to identify unique fields</li>
 *   <li>{@code @DomainField} to map entity fields to domain accessors</li>
 *   <li>{@code @Table(name="...")} to determine the table name</li>
 * </ul>
 * <p>
 * Results are cached per entity class for performance.
 *
 * @param <DOMAIN>    the domain type
 * @param <ENTITY>    the entity type
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConstraintRetriever<DOMAIN, ENTITY> {
    private final EntityManager entityManager;
    private final Map<String, String> constraintNameCache = new ConcurrentHashMap<>();
    private final Map<Class<ENTITY>, Set<ConstraintMapping<DOMAIN>>> entityConstraintCache = new ConcurrentHashMap<>();

    /**
     * Extracts unique field constraints from an entity class.
     *
     * @param entityClass the JPA entity class to scan
     * @return a set of field constraints
     */
    public Set<ConstraintMapping<DOMAIN>> extractConstraintsFromEntity(Class<ENTITY> entityClass) {
        // check cache first
        if (entityConstraintCache.containsKey(entityClass)) {
            return entityConstraintCache.get(entityClass);
        }

        // extract table name
        String tableName = JpaUtils.extractTableNameFromEntity(entityClass);
        log.debug("Extracting constraints for entity {} (table: {})", entityClass.getSimpleName(), tableName);

        // find all fields with @Column(unique=true) and @DomainField
        Set<ConstraintMapping<DOMAIN>> constraints = Stream.of(entityClass.getDeclaredFields())
                .filter(JpaUtils::isUniqueField)
                .filter(field -> field.isAnnotationPresent(DomainField.class))
                .map(field -> createFieldConstraint(field, tableName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        log.info("Found {} unique field constraint(s) for {}: {}",
                constraints.size(),
                entityClass.getSimpleName(),
                constraints.stream()
                        .map(ConstraintMapping::columnName)
                        .collect(Collectors.joining(", "))
        );

        // cache the result
        entityConstraintCache.put(entityClass, constraints);

        return constraints;
    }

    /**
     * Creates a FieldConstraint for a specific field.
     */
    private Optional<ConstraintMapping<DOMAIN>> createFieldConstraint(
            Field field,
            String tableName
    ) {
        try {
            // get column name from @Column annotation
            String columnName = JpaUtils.extractColumnName(field);

            // get domain field name from @DomainField annotation
            DomainField domainFieldAnnotation = field.getAnnotation(DomainField.class);
            String domainFieldName = domainFieldAnnotation.value();

            // resolve constraint name from database
            Optional<String> constraintName = getConstraintName(tableName, columnName);
            if (constraintName.isEmpty()) {
                log.warn("Could not resolve constraint name for {}.{}, skipping field", tableName, columnName);
                return Optional.empty();
            }

            ConstraintMapping<DOMAIN> constraint = new ConstraintMapping<>(
                    domainFieldName, columnName,
                    constraintName.get()
            );

            log.debug("Created constraint: {} -> {} ({})", columnName, constraintName.get(), domainFieldName);

            return Optional.of(constraint);
        } catch (Exception e) {
            log.error("Failed to create field constraint for field {}", field.getName(), e);
            return Optional.empty();
        }
    }

    /**
     * Retrieves the constraint name for a uniqueness constraint on a specific table column.
     *
     * @param tableName  the database table name
     * @param columnName the database column name
     * @return the constraint name, or empty if not found
     */
    private Optional<String> getConstraintName(String tableName, String columnName) {

        // check constraint cache first
        String cacheKey = tableName + "." + columnName;
        if (constraintNameCache.containsKey(cacheKey)) {
            return Optional.ofNullable(constraintNameCache.get(cacheKey));
        }

        // query database metadata
        Optional<String> constraintName;
        try {
            constraintName = entityManager.unwrap(Session.class).doReturningWork(connection -> {
                DatabaseMetaData metaData = connection.getMetaData();

                // query for unique constraints (indexes)
                try (ResultSet rs = metaData.getIndexInfo(
                        connection.getCatalog(),
                        connection.getSchema(),
                        tableName,
                        true, // unique indexes only
                        false // approximate results not ok
                )) {
                    while (rs.next()) {
                        String indexColumnName = rs.getString("COLUMN_NAME");
                        String indexName = rs.getString("INDEX_NAME");

                        if (columnName.equals(indexColumnName) && indexName != null) {
                            log.debug("Found constraint '{}' for {}.{}", indexName, tableName, columnName);
                            return Optional.of(indexName);
                        }
                    }
                }

                log.warn("No unique constraint found for {}.{}", tableName, columnName);
                return Optional.empty();
            });
        } catch (Exception e) {
            log.error("Failed to resolve constraint name for {}.{}", tableName, columnName, e);
            return Optional.empty();
        }

        // cache the result (including null results to avoid repeated queries)
        constraintNameCache.put(cacheKey, constraintName.orElse(null));

        return constraintName;
    }
}
