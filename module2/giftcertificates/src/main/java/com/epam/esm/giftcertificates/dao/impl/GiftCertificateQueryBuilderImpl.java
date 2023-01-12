package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateQueryBuilder;
import com.epam.esm.giftcertificates.dao.util.GiftCertificateQueriesUtil;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateQueryBuilderImpl implements GiftCertificateQueryBuilder {

    private boolean searchByTagNameApplied = false;
    private boolean sortByNameApplied = false;
    private final StringBuilder query = new StringBuilder();
    private final List<Object> parametersValues = new ArrayList<>();
    private final List<Integer> parametersTypes = new ArrayList<>();

    @Override
    public String getQuery() {
        return query.toString();
    }

    @Override
    public Object[] getParametersValues() {
        return parametersValues.toArray();
    }

    @Override
    public int[] getParametersTypes() {
        var arrayParametersTypes = new int[parametersTypes.size()];
        for (int i = 0; i < parametersTypes.size(); i++) {
            arrayParametersTypes[i] = parametersTypes.get(i);
        }
        return arrayParametersTypes;
    }

    @Override
    public GiftCertificateQueryBuilder applySearchByTagName(String tagName) {
        if (tagName != null) {
            query.append(GiftCertificateQueriesUtil.SELECT_GIFT_CERTIFICATES_BY_TAG_NAME);
            parametersValues.add(tagName);
            parametersTypes.add(Types.VARCHAR);
            searchByTagNameApplied = true;
        } else {
            query.append(GiftCertificateQueriesUtil.SELECT_GIFT_CERTIFICATES_BY_PARAMETERS);
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySearchByPartOfName(String partName) {
        if (partName != null) {
            if (searchByTagNameApplied) {
                query.append(" ")
                        .append(GiftCertificateQueriesUtil.ADD_AND);
            } else {
                query.append(" ")
                        .append(GiftCertificateQueriesUtil.ADD_WHERE);
            }
            query.append(" ")
                    .append(GiftCertificateQueriesUtil.ADD_SELECT_BY_PART_OF_NAME);
            parametersValues.add(partName + "%");
            parametersTypes.add(Types.VARCHAR);
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySearchByPartOfDescription(String partDescription) {
        if (partDescription != null) {
            if (searchByTagNameApplied) {
                query.append(" ")
                        .append(GiftCertificateQueriesUtil.ADD_AND);
            } else {
                query.append(" ")
                        .append(GiftCertificateQueriesUtil.ADD_WHERE);
            }
            query.append(" ")
                    .append(GiftCertificateQueriesUtil.ADD_SELECT_BY_PART_OF_DESCRIPTION);
            parametersValues.add(partDescription + "%");
            parametersTypes.add(Types.VARCHAR);
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySortByName(String order) {
        if (order != null) {
            query.append(" ")
                    .append(GiftCertificateQueriesUtil.ADD_ORDER_BY)
                    .append(" ")
                    .append(GiftCertificateQueriesUtil.ADD_NAME)
                    .append(" ")
                    .append(order);
            sortByNameApplied = true;
        }
        return this;
    }

    @Override
    public GiftCertificateQueryBuilder applySortByDate(String order) {
        if (order != null) {
            if (sortByNameApplied) {
                query.append(", ");
            } else {
                query.append(" ")
                        .append(GiftCertificateQueriesUtil.ADD_ORDER_BY)
                        .append(" ");
            }
            query.append(GiftCertificateQueriesUtil.ADD_DATE)
                    .append(" ")
                    .append(order);
        }
        return this;
    }

    @Override
    public void applyPagination(int pageNumber) {
        var offset = (pageNumber - 1) * GiftCertificateQueriesUtil.LIMIT;
        query.append(" ").append(GiftCertificateQueriesUtil.ADD_PAGINATION);
        parametersValues.addAll(List.of(offset, GiftCertificateQueriesUtil.LIMIT));
        parametersTypes.addAll(List.of(Types.INTEGER, Types.INTEGER));
    }
}
