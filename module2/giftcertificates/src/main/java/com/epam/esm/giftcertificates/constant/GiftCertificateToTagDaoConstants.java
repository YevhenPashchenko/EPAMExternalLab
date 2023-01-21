package com.epam.esm.giftcertificates.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GiftCertificateToTagDaoConstants {

    public final String CREATE_GIFT_CERTIFICATE_TO_TAG_LINK_QUERY =
            "INSERT INTO gift_certificates.gift_certificate_tags VALUES (?, ?)";
}
