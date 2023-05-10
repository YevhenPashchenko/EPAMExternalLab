package com.epam.esm.giftcertificates.integration.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUrls {

    public final String VALID_PAGINATION_URL = "?page=0&size=2";
    public final String NOT_VALID_PAGE_PAGINATION_URL = "?page=-1&size=2";
    public final String NOT_VALID_SIZE_PAGINATION_URL = "?page=0&size=0";

    public final String GIFT_CERTIFICATES_URL = "/gift-certificates";
    public final String SORT_URL = "/sort";

    public final String RECEIPTS_URL = "/receipts";

    public final String TAGS_URL = "/tags";
    public final String WIDELY_USED_URL = "/widely-used";
}
