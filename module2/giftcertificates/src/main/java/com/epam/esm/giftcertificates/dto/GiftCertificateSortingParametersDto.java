package com.epam.esm.giftcertificates.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class GiftCertificateSortingParametersDto {

    private String tagName;
    private String partName;
    private String partDescription;
    private String sortByNameOrder;
    private String sortByDateOrder;
}
