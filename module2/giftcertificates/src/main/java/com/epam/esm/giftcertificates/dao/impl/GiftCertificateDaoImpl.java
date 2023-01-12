package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateDao;
import com.epam.esm.giftcertificates.dao.GiftCertificateQueryBuilder;
import com.epam.esm.giftcertificates.dao.util.GiftCertificateQueriesUtil;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.GiftCertificateSortingParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateQueryBuilder giftCertificateQueryBuilder;

    @Override
    public long create(GiftCertificate giftCertificate) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(GiftCertificateQueriesUtil.CREATE_GIFT_CERTIFICATE_QUERY,
                    new String[] {"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setInt(4, giftCertificate.getDuration());
            ps.setObject(5, LocalDateTime.now());
            ps.setObject(6, LocalDateTime.now());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<GiftCertificate> get(int pageNumber) {
        int offset = (pageNumber - 1) * GiftCertificateQueriesUtil.LIMIT;
        return jdbcTemplate.query(GiftCertificateQueriesUtil.SELECT_ALL_GIFT_CERTIFICATES_ON_PAGE_QUERY,
                new Object[] {offset, GiftCertificateQueriesUtil.LIMIT},
                new int[] {Types.INTEGER, Types.INTEGER},
                new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public Optional<GiftCertificate> getById(long id) {
        return jdbcTemplate.query(GiftCertificateQueriesUtil.SELECT_GIFT_CERTIFICATE_BY_ID_QUERY, new Object[] {id},
                new int[] {Types.BIGINT}, new BeanPropertyRowMapper<>(GiftCertificate.class)).stream().findAny();
    }

    @Override
    public List<GiftCertificate> getByParameters(int pageNumber, GiftCertificateSortingParameters giftCertificateSortingParameters) {
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        return jdbcTemplate.query(giftCertificateQueryBuilder.getQuery(),
                giftCertificateQueryBuilder.getParametersValues(),
                giftCertificateQueryBuilder.getParametersTypes(),
                new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public void updateName(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateQueriesUtil.CHANGE_GIFT_CERTIFICATE_NAME_QUERY, giftCertificate.getName(),
                LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void updateDescription(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateQueriesUtil.CHANGE_GIFT_CERTIFICATE_DESCRIPTION_QUERY,
                giftCertificate.getDescription(), LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void updatePrice(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateQueriesUtil.CHANGE_GIFT_CERTIFICATE_PRICE_QUERY, giftCertificate.getPrice(),
                LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void updateDuration(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateQueriesUtil.CHANGE_GIFT_CERTIFICATE_DURATION_QUERY,
                giftCertificate.getDuration(), LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(GiftCertificateQueriesUtil.DELETE_GIFT_CERTIFICATE_QUERY, id);
    }
}
