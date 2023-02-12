package com.epam.esm.giftcertificates.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

  @Valid private List<TagDto> tags = new ArrayList<>();

  private Long id;

  @NotBlank(message = "name must be not empty")
  private String name;

  @NotBlank(message = "description must be not empty")
  private String description;

  @Min(value = 0, message = "price must be positive value")
  private BigDecimal price;

  @Min(value = 0, message = "duration must be positive value")
  private Integer duration;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    GiftCertificateDto that = (GiftCertificateDto) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
