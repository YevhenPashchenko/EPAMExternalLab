package com.epam.esm.giftcertificates.entity;

import lombok.Data;

@Data
public class GiftCertificateSortingParameters {

    private String tagName;
    private String partName;
    private String partDescription;
    private String sortByNameOrder;
    private String sortByDateOrder;
}
