package com.epam.esm.giftcertificates.integration.constant;

public class IntegrationTestConstant {

    public static final String POSTGRES_IMAGE_VERSION = "postgres:14.1";
    public static final String DATABASE_NAME = "giftcertificates";
    public static final String USER_NAME = "postgres";
    public static final String USER_PASSWORD = "postgres";
    public static final String CLEAR_DATABASE_QUERY = "TRUNCATE gift_certificates.gift_certificate, gift_certificates.tag CASCADE";
    public static final String CREATE_GIFT_CERTIFICATE_URL = "/certificates";
    public static final String GET_LIST_OF_GIFT_CERTIFICATES_FOR_PAGE_URL = "/certificates/page/{number}";
    public static final String GET_GIFT_CERTIFICATE_BY_ID_URL = "/certificates/{id}";
    public static final String GET_LIST_OF_GIFT_CERTIFICATES_BY_PARAMETERS_FOR_PAGE_URL = "/certificates/sort/page/{number}";
    public static final String PATCH_GIFT_CERTIFICATE_URL = "/certificates";
    public static final String DELETE_GIFT_CERTIFICATE_URL = "/certificates/{id}";
    public static final String CREATE_TAG_URL = "/tags";
    public static final String GET_LIST_OF_TAGS_URL = "/tags";
    public static final String GET_TAG_BY_ID_URL = "/tags/{id}";
    public static final String DELETE_TAG_URL = "/tags/{id}";
    public static final String PATH_TO_FIRST_TEST_GIFT_CERTIFICATE_JSON = "json/firstTestGiftCertificateJson.json";
    public static final String PATH_TO_SECOND_TEST_GIFT_CERTIFICATE_JSON = "json/secondTestGiftCertificateJson.json";
    public static final String PATH_TO_TEST_GIFT_CERTIFICATE_PARAMETERS_JSON = "json/testGiftCertificateParameters.json";
    public static final String PATH_TO_TEST_GIFT_CERTIFICATE_FOR_UPDATE_JSON = "json/testGiftCertificateForUpdate.json";
    public static final String PATH_TO_TAG_JSON = "json/testTagJson.json";
    public static final String GIFT_CERTIFICATE_FIELD_ID_KEY = "id";
    public static final String GIFT_CERTIFICATE_FIELD_NAME_KEY = "name";
    public static final String GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY = "description";
    public static final String GIFT_CERTIFICATE_FIELD_PRICE_KEY = "price";
    public static final String GIFT_CERTIFICATE_FIELD_DURATION_KEY = "duration";
    public static final String GIFT_CERTIFICATE_PARAMETERS_FIELD_PART_NAME_KEY = "part-name";
    public static final String TAG_FIELD_ID_KEY = "id";
    public static final String TAG_FIELD_NAME_KEY = "name";
}
