package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateToTagDAO;
import com.epam.esm.giftcertificates.entities.GiftCertificateToTag;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
@AllArgsConstructor
public class GiftCertificateToTagDAOImpl implements GiftCertificateToTagDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO gc_schema.gc_tag VALUES (?, ?)";

    @Override
    public void create(GiftCertificateToTag giftCertificateToTag) {
        jdbcTemplate.update(CREATE, new Object[] {giftCertificateToTag.getGiftCertificateId(), giftCertificateToTag.getTagId()},
                new int[] {Types.BIGINT, Types.BIGINT});
    }
}
