package de.seuhd.campuscoffee.data.persistence.repositories;

import de.seuhd.campuscoffee.data.util.JpaUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Generic implementation for sequence resetting that automatically determines
 * the sequence name based on the entity's table name.
 * This is configured as the base repository class for all repositories used in CampusCoffee.
 */
@NoRepositoryBean
public class ResettableSequenceRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements ResettableSequenceRepository {

    private final EntityManager entityManager;
    private final Class<T> domainClass;

    public ResettableSequenceRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.domainClass = entityInformation.getJavaType();
    }

    /**
     * Resets the database sequence for this entity's ID generation to start from 1.
     * The sequence name is automatically determined from the entity's table name
     * using the pattern: {table_name}_seq
     */
    @Override
    @Transactional
    public void resetSequence() {
        String tableName = JpaUtils.extractTableNameFromEntity(domainClass);
        String sequenceName = tableName + "_seq";
        String sql = "ALTER SEQUENCE " + sequenceName + " RESTART WITH 1";
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
