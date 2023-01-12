package com.epam.esm.giftcertificates.dao;

import java.sql.PreparedStatement;

/**
 * Interface that provides methods that must be implemented for build complex query to database.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateQueryBuilder {

    /**
     * Method that returns built query as a string.
     * @return query as a string.
     */
    String getQuery();

    /**
     * Method that returns set of parameter values which need to put in {@link PreparedStatement} as array of objects.
     * @return array of objects.
     */
    Object[] getParametersValues();

    /**
     * Method that returns set of parameter types as array of integers.
     * @return array of integers.
     */
    int[] getParametersTypes();

    /**
     * Method that add to query part which is responsible for search by tag name if it is not null. Also add value of tag
     * name to array of parameter values, and it types as integer presentation to array of parameter types. If tag name is
     * null add to query part which is responsible for search by other parameters.
     * @param tagName tag name for search.
     * @return this {@link GiftCertificateQueryBuilder}.
     */
    GiftCertificateQueryBuilder applySearchByTagName(String tagName);

    /**
     * Method that add to query part which is responsible for search by part of name if it is not null. Also add value of part
     * name to array of parameter values, and it types as integer presentation to array of parameter types.
     * @param partName part of name for search.
     * @return this {@link GiftCertificateQueryBuilder}.
     */
    GiftCertificateQueryBuilder applySearchByPartOfName(String partName);

    /**
     * Method that add to query part which is responsible for search by part of description if it is not null. Also add
     * value of part description to array of parameter values, and it types as integer presentation to array of parameter types.
     * @param partDescription part of description for search.
     * @return this {@link GiftCertificateQueryBuilder}.
     */
    GiftCertificateQueryBuilder applySearchByPartOfDescription(String partDescription);

    /**
     * Method that add to query part which is responsible for sort by name if it is not null.
     * @param order order of sorting by name.
     * @return this {@link GiftCertificateQueryBuilder}.
     */
    GiftCertificateQueryBuilder applySortByName(String order);

    /**
     * Method that add to query part which is responsible for sort by last update date if it is not null.
     * @param order order of sorting by last update date.
     * @return this {@link GiftCertificateQueryBuilder}.
     */
    GiftCertificateQueryBuilder applySortByDate(String order);

    /**
     * Method that add to query part which is responsible for pagination.
     * @param pageNumber current page number.
     */
    void applyPagination(int pageNumber);
}
