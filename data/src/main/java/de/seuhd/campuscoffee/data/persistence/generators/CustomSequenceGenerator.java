package de.seuhd.campuscoffee.data.persistence.generators;

import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.util.Properties;

/**
 * Custom Hibernate ID generator that automatically creates sequence names
 * based on the entity's table name. For a table named "users", this will
 * generate a sequence named "users_seq".
 */
public class CustomSequenceGenerator extends SequenceStyleGenerator {

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        // Hibernate provides the table name from @Table annotation in the params
        // The key is "target_table" in Hibernate's parameter map
        String tableName = params.getProperty("target_table");

        if (tableName != null) {
            // Set sequence name based on table name (e.g., "users" -> "users_seq")
            params.setProperty(SEQUENCE_PARAM, tableName + "_seq");
            params.setProperty(INCREMENT_PARAM, "1");
        }

        super.configure(type, params, serviceRegistry);
    }
}