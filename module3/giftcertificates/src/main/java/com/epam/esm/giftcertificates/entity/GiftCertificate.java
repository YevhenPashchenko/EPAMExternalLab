package com.epam.esm.giftcertificates.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "gift_certificate")
@Getter
@Setter
@RequiredArgsConstructor
public class GiftCertificate extends AbstractEntity {

  @Id
  @GeneratedValue(generator = "gift_certificate_id_seq")
  @SequenceGenerator(
      name = "gift_certificate_id_seq",
      sequenceName = "gift_certificate_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false, unique = true)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Column(name = "duration", nullable = false)
  private Integer duration;

  @ManyToMany
  @JoinTable(
      name = "gift_certificate_tags",
      joinColumns = @JoinColumn(name = "gift_certificate_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

  public GiftCertificate(
      Long id,
      String name,
      String description,
      BigDecimal price,
      Integer duration,
      LocalDateTime createDate,
      LocalDateTime lastUpdateDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.duration = duration;
    super.setCreateDate(createDate);
    super.setLastUpdateDate(lastUpdateDate);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GiftCertificate that = (GiftCertificate) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
