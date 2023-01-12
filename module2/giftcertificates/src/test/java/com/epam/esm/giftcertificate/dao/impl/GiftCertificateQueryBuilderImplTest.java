package com.epam.esm.giftcertificate.dao.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateQueryBuilder;
import com.epam.esm.giftcertificates.dao.impl.GiftCertificateQueryBuilderImpl;
import com.epam.esm.giftcertificates.dao.util.GiftCertificateQueriesUtil;
import com.epam.esm.giftcertificates.entities.GiftCertificateSortingParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Types;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateQueryBuilderImplTest {

    public static GiftCertificateSortingParameters giftCertificateSortingParameters;
    public static int pageNumber = 1;
    private GiftCertificateQueryBuilder giftCertificateQueryBuilder;

    @BeforeAll
    static void createNewGiftCertificateSortingParameters() {
        giftCertificateSortingParameters = new GiftCertificateSortingParameters();
        giftCertificateSortingParameters.setTagName("tagName");
        giftCertificateSortingParameters.setPartName("partName");
        giftCertificateSortingParameters.setPartDescription("partDescription");
        giftCertificateSortingParameters.setSortByNameOrder("asc");
        giftCertificateSortingParameters.setSortByDateOrder("desc");
    }

    @BeforeEach
    void createNewQueryBuilder() {
        giftCertificateQueryBuilder = new GiftCertificateQueryBuilderImpl();
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartNameNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartDescriptionNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenSortByNameOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartNameNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.name LIKE ? OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartDescriptionNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.description LIKE ? OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndSortByNameOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartNameAndSortByNameOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartNameAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartDescriptionAndSortByNameOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartDescriptionAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartNameAndSortByNameOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.name LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartNameAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.name LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartDescriptionAndSortByNameOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.description LIKE ? ORDER BY gift_certificate.name asc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(null)
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartDescriptionAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.description LIKE ? ORDER BY gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(null)
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartNameAndSortByNameOrderAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.name LIKE ? " +
                "ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenPartDescriptionAndSortByNameOrderAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = "SELECT gift_certificate.* FROM gc_schema.gift_certificate " +
                "WHERE gift_certificate.description LIKE ? " +
                "ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(null)
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartNameAndSortByNameOrderAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.name LIKE ? ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartName() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(giftCertificateSortingParameters.getPartName())
                .applySearchByPartOfDescription(null)
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }

    @Test
    void giftCertificateQueryBuilderChainCalls_whenTagNameAndPartDescriptionAndSortByNameOrderAndSortByDateOrderNotNull_thenQueryAndArraysEqualsExpected() {
        //GIVEN
        var expectedQuery = """
            SELECT gift_certificate.* FROM gc_schema.gift_certificate
            INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id
            INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id
            WHERE tag.name = ? AND gift_certificate.description LIKE ? ORDER BY gift_certificate.name asc, gift_certificate.last_update_date desc OFFSET ? LIMIT ?""";
        var offset = 0;
        var expectedParametersValuesArray = new Object[] {
                giftCertificateSortingParameters.getTagName(),
                giftCertificateSortingParameters.getPartDescription() + "%",
                offset,
                GiftCertificateQueriesUtil.LIMIT
        };
        var expectedParametersTypesArray = new int[] {
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER
        };

        //WHEN
        giftCertificateQueryBuilder.applySearchByTagName(giftCertificateSortingParameters.getTagName())
                .applySearchByPartOfName(null)
                .applySearchByPartOfDescription(giftCertificateSortingParameters.getPartDescription())
                .applySortByName(giftCertificateSortingParameters.getSortByNameOrder())
                .applySortByDate(giftCertificateSortingParameters.getSortByDateOrder())
                .applyPagination(pageNumber);

        //THEN
        assertEquals(expectedQuery, giftCertificateQueryBuilder.getQuery());
        for (int i = 0; i < expectedParametersValuesArray.length; i++) {
            assertEquals(expectedParametersValuesArray[i], giftCertificateQueryBuilder.getParametersValues()[i]);
        }
        for (int i = 0; i < expectedParametersTypesArray.length; i++) {
            assertEquals(expectedParametersTypesArray[i], giftCertificateQueryBuilder.getParametersTypes()[i]);
        }
    }
}
