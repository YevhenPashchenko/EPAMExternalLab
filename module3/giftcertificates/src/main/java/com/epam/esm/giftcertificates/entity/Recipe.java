package com.epam.esm.giftcertificates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "recipe")
@Getter
@Setter
public class Recipe extends AbstractEntity {

  @Id
  @GeneratedValue(generator = "recipe_id_seq")
  @SequenceGenerator(name = "recipe_id_seq", sequenceName = "recipe_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "total_cost", nullable = false, scale = 2)
  private BigDecimal totalCost;

  @ManyToMany
  @JoinTable(
      name = "gift_certificates_recipe",
      joinColumns = @JoinColumn(name = "recipe_id"),
      inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
  private List<GiftCertificate> giftCertificates = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Recipe recipe = (Recipe) o;
    return Objects.equals(id, recipe.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
