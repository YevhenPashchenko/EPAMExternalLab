package com.epam.esm.giftcertificates.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderDto extends RepresentationModel<OrderDto> {

  @NotEmpty private List<GiftCertificateDto> giftCertificates = new ArrayList<>();

  private Long id;
  private BigDecimal totalCost;
  private LocalDateTime createDate;
  private UserDto userDto;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    OrderDto orderDto = (OrderDto) o;
    return Objects.equals(id, orderDto.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
