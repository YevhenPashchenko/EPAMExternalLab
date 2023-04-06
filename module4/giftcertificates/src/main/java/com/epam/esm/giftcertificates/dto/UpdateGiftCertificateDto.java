package com.epam.esm.giftcertificates.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UpdateGiftCertificateDto {

    @NotEmpty(message = "List of tag names must not be empty")
    private Set<TagDto> tags = new HashSet<>();
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
}
