package com.epam.esm.giftcertificates.dao.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GiftCertificateQueriesUtil {

    public final String CREATE_GIFT_CERTIFICATE_QUERY =
            "INSERT INTO gc_schema.gift_certificate VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    public final int LIMIT = 3;
    public final String SELECT_ALL_GIFT_CERTIFICATES_ON_PAGE_QUERY =
            "SELECT * FROM gc_schema.gift_certificate OFFSET ? LIMIT ?";
    public final String SELECT_GIFT_CERTIFICATE_BY_ID_QUERY = "SELECT * FROM gc_schema.gift_certificate WHERE id = ?";
    public final String SELECT_GIFT_CERTIFICATES_BY_TAG_NAME = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ?""";
    public final String SELECT_GIFT_CERTIFICATES_BY_PARAMETERS =
            "SELECT gift_certificate.* FROM gc_schema.gift_certificate";
    public final String ADD_AND = "AND";
    public final String ADD_WHERE = "WHERE";
    public final String ADD_SELECT_BY_PART_OF_NAME = "gift_certificate.name LIKE ?";
    public final String ADD_SELECT_BY_PART_OF_DESCRIPTION = "gift_certificate.description LIKE ?";
    public final String ADD_ORDER_BY = "ORDER BY";
    public final String ADD_NAME = "gift_certificate.name";
    public final String ADD_DATE = "gift_certificate.last_update_date";
    public final String ADD_PAGINATION = "OFFSET ? LIMIT ?";
    public final String CHANGE_GIFT_CERTIFICATE_NAME_QUERY =
            "UPDATE gc_schema.gift_certificate SET name = ?, last_update_date = ? WHERE id = ?";
    public final String CHANGE_GIFT_CERTIFICATE_DESCRIPTION_QUERY =
            "UPDATE gc_schema.gift_certificate SET description = ?, last_update_date = ? WHERE id = ?";
    public final String CHANGE_GIFT_CERTIFICATE_PRICE_QUERY =
            "UPDATE gc_schema.gift_certificate SET price = ?, last_update_date = ? WHERE id = ?";
    public final String CHANGE_GIFT_CERTIFICATE_DURATION_QUERY =
            "UPDATE gc_schema.gift_certificate SET duration = ?, last_update_date = ? WHERE id = ?";
    public final String DELETE_GIFT_CERTIFICATE_QUERY = "DELETE FROM gc_schema.gift_certificate WHERE id = ?";
}
