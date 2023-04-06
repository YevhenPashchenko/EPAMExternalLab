package com.epam.esm.giftcertificates.dto;

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

    private List<GiftCertificateDto> giftCertificates = new ArrayList<>();

    private BigDecimal totalCost;
    private LocalDateTime createDate;

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
        ReceiptDto that = (ReceiptDto) o;
        return Objects.equals(giftCertificates, that.giftCertificates) && Objects.equals(totalCost,
            that.totalCost) && Objects.equals(createDate, that.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), giftCertificates, totalCost, createDate);
    }
}
