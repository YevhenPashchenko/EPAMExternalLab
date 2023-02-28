package com.epam.esm.giftcertificates.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ReceiptDto extends RepresentationModel<ReceiptDto> {

    @NotEmpty(message = "list of gift certificates must not be empty")
    private List<GiftCertificateDto> giftCertificates = new ArrayList<>();

    private Long id;
    private BigDecimal totalCost;
    private LocalDateTime createDate;
    private PersonDto personDto;

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
        ReceiptDto receiptDto = (ReceiptDto) o;
        return Objects.equals(id, receiptDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
