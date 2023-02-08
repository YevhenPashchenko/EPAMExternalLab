package com.epam.esm.giftcertificates.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class GiftCertificateDtoForUpdate {

  private Long id;
  private String name;
  private String description;
  private BigDecimal price;
  private Integer duration;
}
