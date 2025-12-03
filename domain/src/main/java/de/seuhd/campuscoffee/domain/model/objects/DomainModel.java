package de.seuhd.campuscoffee.domain.model.objects;

import java.io.Serializable;

/**
 * Base interface for all domain model objects.
 * This interface extends Serializable to allow cloning of domain objects
 * and Identifiable to provide a standard way to access the unique identifier.
 */
public interface DomainModel<ID>
        extends Serializable, // serializable to allow cloning (see TestFixtures class)
                Identifiable<ID> {
}
