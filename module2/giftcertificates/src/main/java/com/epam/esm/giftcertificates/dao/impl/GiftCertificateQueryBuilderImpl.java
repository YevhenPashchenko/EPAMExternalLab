package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.constant.DaoConstants;
import com.epam.esm.giftcertificates.dao.GiftCertificateQueryBuilder;
import com.epam.esm.giftcertificates.constant.GiftCertificateQueryBuilderConstants;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateQueryBuilderImpl implements GiftCertificateQueryBuilder {

    private boolean searchByTagNameApplied = false;
    private boolean sortByNameApplied = false;
    private final StringBuilder queryBuilder = new StringBuilder();
    private final List<Object> parametersValuesList = new ArrayList<>();
    private final List<Integer> parametersTypesList = new ArrayList<>();
    private String query;
    private Object[] parametersValues;
    private int[] parametersTypes;

    public static GiftCertificateQueryBuilderImpl builder() {
        return new GiftCertificateQueryBuilderImpl();
    }

    @Override
    public GiftCertificateQueryBuilder applySearchByTagName(String tagName) {
        if (tagName != null) {
            queryBuilder.append(GiftCertificateQueryBuilderConstants.SELECT_GIFT_CERTIFICATES_BY_TAG_NAME_QUERY);
            parametersValuesList.add(tagName);
            parametersTypesList.add(Types.VARCHAR);
            searchByTagNameApplied = true;
        } else {
            queryBuilder.append(GiftCertificateQueryBuilderConstants.SELECT_ALL_GIFT_CERTIFICATES_QUERY);
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySearchByPartOfName(String partName) {
        if (partName != null) {
            if (searchByTagNameApplied) {
                queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                        .append(GiftCertificateQueryBuilderConstants.AND);
            } else {
                queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                        .append(GiftCertificateQueryBuilderConstants.WHERE);
            }
            queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(GiftCertificateQueryBuilderConstants.GIFT_CERTIFICATE_DOT_NAME)
                    .append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(GiftCertificateQueryBuilderConstants.LIKE);
            parametersValuesList.add(partName + GiftCertificateQueryBuilderConstants.PERCENT_WILDCARD);
            parametersTypesList.add(Types.VARCHAR);
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySearchByPartOfDescription(String partDescription) {
        if (partDescription != null) {
            if (searchByTagNameApplied) {
                queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                        .append(GiftCertificateQueryBuilderConstants.AND);
            } else {
                queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                        .append(GiftCertificateQueryBuilderConstants.WHERE);
            }
            queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(GiftCertificateQueryBuilderConstants.GIFT_CERTIFICATE_DOT_DESCRIPTION)
                    .append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(GiftCertificateQueryBuilderConstants.LIKE);
            parametersValuesList.add(partDescription + GiftCertificateQueryBuilderConstants.PERCENT_WILDCARD);
            parametersTypesList.add(Types.VARCHAR);
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySortByName(String order) {
        if (order != null) {
            queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(GiftCertificateQueryBuilderConstants.ORDER_BY)
                    .append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(GiftCertificateQueryBuilderConstants.GIFT_CERTIFICATE_DOT_NAME)
                    .append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(order);
            sortByNameApplied = true;
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySortByDate(String order) {
        if (order != null) {
            if (sortByNameApplied) {
                queryBuilder.append(GiftCertificateQueryBuilderConstants.COMMA);
            } else {
                queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                        .append(GiftCertificateQueryBuilderConstants.ORDER_BY)
                        .append(GiftCertificateQueryBuilderConstants.WHITESPACE);
            }
            queryBuilder.append(GiftCertificateQueryBuilderConstants.GIFT_CERTIFICATE_DOT_LAST_UPDATE_DATE)
                    .append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(order);
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applyPagination(int pageNumber) {
        if (pageNumber > 0) {
            var offset = (pageNumber - 1) * DaoConstants.LIMIT;
            queryBuilder.append(GiftCertificateQueryBuilderConstants.WHITESPACE)
                    .append(GiftCertificateQueryBuilderConstants.PAGINATION);
            parametersValuesList.addAll(List.of(offset, DaoConstants.LIMIT));
            parametersTypesList.addAll(List.of(Types.INTEGER, Types.INTEGER));
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilderImpl build() {
        query = queryBuilder.toString();
        parametersValues = parametersValuesList.toArray();
        parametersTypes = getParametersTypesArray();
        return this;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public Object[] getParametersValues() {
        return parametersValues;
    }

    @Override
    public int[] getParametersTypes() {
        return parametersTypes;
    }

    private int[] getParametersTypesArray() {
        var arrayParametersTypes = new int[parametersTypesList.size()];
        for (int i = 0; i < parametersTypesList.size(); i++) {
            arrayParametersTypes[i] = parametersTypesList.get(i);
        }
        return arrayParametersTypes;
    }
}
