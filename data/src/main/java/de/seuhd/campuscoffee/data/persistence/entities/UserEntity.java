package de.seuhd.campuscoffee.data.persistence.entities;

import de.seuhd.campuscoffee.data.constraints.DomainField;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Database entity for a registered user.
 */
@jakarta.persistence.Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity extends Entity {
    public static final String LOGIN_NAME_COLUMN = "login_name";
    public static final String EMAIL_ADDRESS_COLUMN = "email_address";

    @Column(name = LOGIN_NAME_COLUMN, unique = true)
    @DomainField("loginName")
    private String loginName;

    @Column(name = EMAIL_ADDRESS_COLUMN, unique = true)
    @DomainField("emailAddress")
    private String emailAddress;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
}
