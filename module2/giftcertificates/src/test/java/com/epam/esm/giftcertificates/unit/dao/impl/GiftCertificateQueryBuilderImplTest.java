package com.epam.esm.giftcertificates.unit.dao.impl;

import com.epam.esm.giftcertificates.constant.DaoConstants;
import com.epam.esm.giftcertificates.dao.impl.GiftCertificateQueryBuilderImpl;
import com.epam.esm.giftcertificates.entity.GiftCertificateSortingParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateQueryBuilderImplTest {

    public static final GiftCertificateSortingParameters giftCertificateSortingParameters = new GiftCertificateSortingParameters();
    private final int pageNumber = 1;

    @BeforeAll
    static void createNewGiftCertificateSortingParameters() {
        giftCertificateSortingParameters.setTagName("tagName");
        giftCertificateSortingParameters.setPartName("partName");
        giftCertificateSortingParameters.setPartDescription("partDescription");
        giftCertificateSortingParameters.setSortByNameOrder("asc");
        giftCertificateSortingParameters.setSortByDateOrder("desc");
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartNameNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartDescriptionNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenSortByNameNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenSortByDateNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartNameNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.name LIKE ? OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartDescriptionNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.description LIKE ? OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndSortByNameNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartNameAndSortByNameNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartNameAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartDescriptionAndSortByNameNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartDescriptionAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartNameAndSortByNameNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.name LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartNameAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.name LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartDescriptionAndSortByNameNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.description LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartDescriptionAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.description LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartNameAndSortByNameAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? " +
                "ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenPartDescriptionAndSortByNameAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gift_certificates.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? " +
                "ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartNameAndSortByNameAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.name LIKE ? ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_shouldDoQueryAndArraysEqualsExpected_whenTagNameAndPartDescriptionAndSortByNameAndSortByDateNotNull() {
        //GIVEN
        var expectedQuery = """
                SELECT gift_certificate.* FROM gift_certificates.gift_certificate
                INNER JOIN gift_certificates.gift_certificate_tags ON gift_certificate.id = gift_certificate_tags.gift_certificate_id
                INNER JOIN gift_certificates.tag ON gift_certificate_tags.tag_id = tag.id
                WHERE tag.name = ? AND gift_certificate.description LIKE ? ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[]{
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                DaoConstants.LIMIT
        };
        var expectedParametersTypesArray = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        var result = GiftCertificateQueryBuilderImpl
                .builder()
                .applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber)
                .build();

        //THEN
        assertAll(
                () -> assertEquals(expectedQuery, result.getQuery()),
                () -> assertArrayEquals(expectedParametersValuesArray, result.getParametersValues()),
                () -> assertArrayEquals(expectedParametersTypesArray, result.getParametersTypes())
        );
    }
}