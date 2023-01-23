package com.epam.esm.giftcertificates.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GiftCertificateQueryBuilderConstants {

    public final String SELECT_GIFT_CERTIFICATES_BY_TAG_NAME_QUERY = """
            SELECT gift_certificate.* FROM gift_certificates.gift_certificate
            INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
            INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
            WHERE tag.name = ?""";
    public final String SELECT_ALL_GIFT_CERTIFICATES_QUERY =
            "SELECT gift_certificate.* FROM gift_certificates.gift_certificate";
    public final String WHITESPACE = " ";
    public final String AND = "AND";
    public final String WHERE = "WHERE";
    public final String GIFT_CERTIFICATE_DOT_NAME = "gift_certificate.name";
    public final String LIKE = "LIKE ?";
    public final String PERCENT_WILDCARD = "%";
    public final String GIFT_CERTIFICATE_DOT_DESCRIPTION = "gift_certificate.description";
    public final String ORDER_BY = "ORDER BY";
    public final String COMMA = ", ";
    public final String GIFT_CERTIFICATE_DOT_LAST_UPDATE_DATE = "gift_certificate.last_update_date";
    public final String PAGINATION = "OFFSET ? LIMIT ?";
}
