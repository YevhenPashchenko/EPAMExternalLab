package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateToTagDao;
import com.epam.esm.giftcertificates.dao.util.GiftCertificateToTagQueriesUtil;
import com.epam.esm.giftcertificates.entities.GiftCertificateToTag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
@RequiredArgsConstructor
public class GiftCertificateToTagDaoImpl implements GiftCertificateToTagDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(GiftCertificateToTag giftCertificateToTag) {
        jdbcTemplate.update(GiftCertificateToTagQueriesUtil.CREATE_GIFT_CERTIFICATE_TO_TAG_LINK_QUERY,
                new Object[] {giftCertificateToTag.getGiftCertificateId(), giftCertificateToTag.getTagId()},
                new int[] {Types.BIGINT, Types.BIGINT});
    }
}
