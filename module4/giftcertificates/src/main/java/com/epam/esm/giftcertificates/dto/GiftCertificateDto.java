package com.epam.esm.giftcertificates.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    @Valid
    private Set<TagDto> tags = new HashSet<>();

    @NotBlank(message = "name must not be empty")
    private String name;

    @NotBlank(message = "description must not be empty")
    private String description;

    @DecimalMin(value = "0.01", message = "price must be greater than 0")
    @Digits(integer = 7, fraction = 2, message = "number is out of range (expected <7 integer>.<2 fraction>)")
    private BigDecimal price;

    @Min(value = 1, message = "duration must be greater than 0")
    private Integer duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GiftCertificateDto that = (GiftCertificateDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
