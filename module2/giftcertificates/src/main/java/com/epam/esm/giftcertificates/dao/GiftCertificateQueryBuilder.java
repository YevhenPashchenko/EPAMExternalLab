package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Tag;

/**
 * The {@link GiftCertificateQueryBuilder} interface provides methods to add to summary complex SQL its part and add
 * arguments bind to this query to list.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateQueryBuilder {

    /**
     * Adds to query part which is responsible for search by {@link Tag} {@code name} and adds value of {@link Tag}
     * {@code name} to list of parameters values and its type as integer presentation to list of parameters types.
     * @param tagName {@link Tag} {@code name}.
     */
    GiftCertificateQueryBuilder applySearchByTagName(String tagName);

    /**
     * Adds to query part which is responsible for search by part of {@link GiftCertificate} {@code name} and adds
     * value of part {@link GiftCertificate} {@code name} to list of parameters values and its type as integer
     * presentation to list of parameters types.
     * @param partName part of {@link GiftCertificate} {@code name}.
     */
    GiftCertificateQueryBuilder applySearchByPartOfName(String partName);

    /**
     * Adds to query part which is responsible for search by part of {@link GiftCertificate} {@code description} and
     * adds value of part {@link GiftCertificate} {@code description} to list of parameters values and its type as
     * integer presentation to list of parameter types.
     * @param partDescription part of {@link GiftCertificate} {@code description}.
     */
    GiftCertificateQueryBuilder applySearchByPartOfDescription(String partDescription);

    /**
     * Adds to query part which is responsible for sorting by {@link GiftCertificate} {@code name}.
     * @param order sorting direction by {@link GiftCertificate} {@code name}.
     */
    GiftCertificateQueryBuilder applySortByName(String order);

    /**
     * Adds to query part which is responsible for sorting by {@link GiftCertificate} {@code lastUpdateDate}.
     * @param order sorting direction by {@link GiftCertificate} {@code lastUpdateDate}.
     */
    GiftCertificateQueryBuilder applySortByDate(String order);

    /**
     * Adds to query part which is responsible for pagination and adds calculating value of {@code offset} and
     * {@code limit} to list of parameter values and their types as integer presentation to list of parameter types.
     * @param pageNumber given {@code page}.
     */
    GiftCertificateQueryBuilder applyPagination(int pageNumber);

    /**
     * Completes building complex SQl.
     * @return {@link GiftCertificateQueryBuilder} object.
     */
    GiftCertificateQueryBuilder build();

    /**
     * Returns query as string.
     * @return query.
     */
    String getQuery();

    /**
     * Returns list of parameters values bind to query as array of objects.
     * @return array of objects.
     */
    Object[] getParametersValues();

    /**
     * Returns list of parameters types bind to query as array of integers.
     * @return array of integers.
     */
    int[] getParametersTypes();
}
