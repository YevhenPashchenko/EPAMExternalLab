package com.epam.esm.giftcertificates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "person")
@Getter
@Setter
public class Person extends AbstractEntity {

  @Id
  @GeneratedValue(generator = "person_id_seq")
  @SequenceGenerator(name = "person_id_seq", sequenceName = "person_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @OneToMany(mappedBy = "person")
  private Set<Recipe> recipes = new HashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person user = (Person) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
