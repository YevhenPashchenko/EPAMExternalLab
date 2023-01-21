package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.constant.DaoConstants;
import com.epam.esm.giftcertificates.dao.GiftCertificateDao;
import com.epam.esm.giftcertificates.constant.GiftCertificateDaoConstants;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.GiftCertificateSortingParameters;
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

    @Override
    public long createGiftCertificate(GiftCertificate giftCertificate) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(GiftCertificateDaoConstants.CREATE_GIFT_CERTIFICATE_QUERY,
                    new String[] {DaoConstants.ID});
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
    public List<GiftCertificate> getListOfGiftCertificateForPage(int pageNumber) {
        var offset = (pageNumber - 1) * DaoConstants.LIMIT;
        return jdbcTemplate.query(GiftCertificateDaoConstants.SELECT_ALL_GIFT_CERTIFICATES_ON_PAGE_QUERY,
                new Object[] {offset, DaoConstants.LIMIT},
                new int[] {Types.INTEGER, Types.INTEGER},
                new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(long id) {
        return jdbcTemplate.query(GiftCertificateDaoConstants.SELECT_GIFT_CERTIFICATE_BY_ID_QUERY,
                new Object[] {id},
                new int[] {Types.BIGINT},
                new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream()
                .findAny();
    }

    @Override
    public List<GiftCertificate> getListOfGiftCertificateByParametersForPage(int pageNumber,
                                                                             GiftCertificateSortingParameters giftCertificateSortingParameters) {
        var giftCertificateQueryBuilder = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();
        return jdbcTemplate.query(giftCertificateQueryBuilder.getQuery(),
                giftCertificateQueryBuilder.getParametersValues(),
                giftCertificateQueryBuilder.getParametersTypes(),
                new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public void updateGiftCertificateName(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateDaoConstants.UPDATE_GIFT_CERTIFICATE_NAME_QUERY, giftCertificate.getName(),
                LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void updateGiftCertificateDescription(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateDaoConstants.UPDATE_GIFT_CERTIFICATE_DESCRIPTION_QUERY,
                giftCertificate.getDescription(), LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void updateGiftCertificatePrice(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateDaoConstants.UPDATE_GIFT_CERTIFICATE_PRICE_QUERY, giftCertificate.getPrice(),
                LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void updateGiftCertificateDuration(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateDaoConstants.UPDATE_GIFT_CERTIFICATE_DURATION_QUERY,
                giftCertificate.getDuration(), LocalDateTime.now(), giftCertificate.getId());
    }

    @Override
    public void deleteGiftCertificate(long id) {
        jdbcTemplate.update(GiftCertificateDaoConstants.DELETE_GIFT_CERTIFICATE_QUERY, id);
    }
}
