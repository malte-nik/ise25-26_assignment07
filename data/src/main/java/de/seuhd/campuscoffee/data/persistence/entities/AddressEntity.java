package de.seuhd.campuscoffee.data.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Embedded database entity for an address.
 */
@Embeddable
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AddressEntity {
    private String street;
    @Column(name = "house_number")
    private Integer houseNumber;
    @Column(name = "house_number_suffix")
    private Character houseNumberSuffix;
    @Column(name = "postal_code")
    private Integer postalCode;
    private String city;
}