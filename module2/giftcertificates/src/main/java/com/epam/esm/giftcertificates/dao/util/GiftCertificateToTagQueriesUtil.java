package com.epam.esm.giftcertificates.dao.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GiftCertificateToTagQueriesUtil {

    public final String CREATE_GIFT_CERTIFICATE_TO_TAG_LINK_QUERY = "INSERT INTO gc_schema.gc_tag VALUES (?, ?)";
}
