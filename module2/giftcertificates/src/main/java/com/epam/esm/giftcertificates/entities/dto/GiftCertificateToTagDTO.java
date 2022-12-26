package com.epam.esm.giftcertificates.entities.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GiftCertificateToTagDTO implements Serializable {

    private long giftCertificateId;
    private long tagId;
}
