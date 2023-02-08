package com.epam.esm.giftcertificates.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
public class TagDto extends RepresentationModel<TagDto> {

  private Long id;

  @NotBlank(message = "name must be not empty")
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    TagDto tagDto = (TagDto) o;
    return Objects.equals(id, tagDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
