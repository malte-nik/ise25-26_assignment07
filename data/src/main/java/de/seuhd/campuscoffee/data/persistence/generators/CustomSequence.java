package de.seuhd.campuscoffee.data.persistence.generators;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for table-based sequence generation.
 * Automatically creates sequence names based on the entity's table name.
 */
@IdGeneratorType(CustomSequenceGenerator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface CustomSequence {
}