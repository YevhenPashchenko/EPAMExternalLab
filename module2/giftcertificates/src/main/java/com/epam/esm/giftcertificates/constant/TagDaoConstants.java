package com.epam.esm.giftcertificates.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TagDaoConstants {

    public final String CREATE_TAG_QUERY = "INSERT INTO gift_certificates.tag VALUES (DEFAULT, ?)";
    public final String SELECT_ALL_TAGS_QUERY = "SELECT * FROM gift_certificates.tag";
    public final String SELECT_TAG_BY_ID_QUERY = "SELECT * FROM gift_certificates.tag WHERE id = ?";
    public final String SELECT_TAGS_BY_GIFT_CERTIFICATE_ID_QUERY = "SELECT tag.* FROM gift_certificates.tag\n" +
            "INNER JOIN gift_certificates.gift_certificate_tags ON tag.id = gift_certificate_tags.tag_id AND gift_certificate_tags.gift_certificate_id = ?";
    public final String DELETE_TAG_QUERY = "DELETE FROM gift_certificates.tag WHERE id = ?";
}
