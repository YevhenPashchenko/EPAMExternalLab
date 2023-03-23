package com.epam.esm.authorization.integration.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUrls {
    public final String LOGIN_URL = "/login";
    public final String CLIENTS_URL = "/clients";
    public final String ALL_URL = "/all";
    public final String VALID_PAGINATION_URL = "?page=0&size=1";
    public final String NOT_VALID_PAGE_PAGINATION_URL = "?page=-1&size=1";
    public final String NOT_VALID_SIZE_PAGINATION_URL = "?page=0&size=0";
    public final String PERSONS_URL = "/persons";
    public final String CHANGE_URL = "/change";
}
