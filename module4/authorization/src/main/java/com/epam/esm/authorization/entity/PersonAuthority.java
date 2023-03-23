package com.epam.esm.authorization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity(name = "person_authorities")
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "person_authorities_unique", columnNames = {"person_email", "authority"})
})
public class PersonAuthority {

    private static final String SEQUENCE_NAME = "person_authorities_id_seq";

    @Id
    @GeneratedValue(generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "person_email", nullable = false, referencedColumnName = "email")
    private Person person;
    @Column(name = "authority", nullable = false)
    private String authority;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonAuthority that = (PersonAuthority) o;
        return Objects.equals(id, that.id) && Objects.equals(person, that.person)
            && Objects.equals(authority, that.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, authority);
    }
}
