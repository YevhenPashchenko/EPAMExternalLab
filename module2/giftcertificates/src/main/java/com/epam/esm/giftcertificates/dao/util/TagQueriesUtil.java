package com.epam.esm.giftcertificates.dao.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TagQueriesUtil {

    public final String CREATE_TAG_QUERY = "INSERT INTO gc_schema.tag VALUES (DEFAULT, ?)";
    public final String SELECT_ALL_TAGS_QUERY = "SELECT * FROM gc_schema.tag";
    public final String SELECT_TAG_BY_ID_QUERY = "SELECT * FROM gc_schema.tag WHERE id = ?";
    public final String SELECT_TAGS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.* FROM gc_schema.tag\n" +
            "INNER JOIN gc_schema.gc_tag ON tag.id = gc_tag.tag_id AND gc_tag.gc_id = ?";
    public final String DELETE_TAG_QUERY = "DELETE FROM gc_schema.tag WHERE id = ?";
}
