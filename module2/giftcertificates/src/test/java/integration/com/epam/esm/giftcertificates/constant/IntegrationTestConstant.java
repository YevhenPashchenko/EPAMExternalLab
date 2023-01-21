package integration.com.epam.esm.giftcertificates.constant;

public class IntegrationTestConstant {

    public static final String POSTGRES_IMAGE_VERSION = "postgres:14.1";
    public static final String DATABASE_NAME = "entities";
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
    public static final String PATH_TO_TEST_JSON_OBJECTS = "json/testJsonObjects.json";
    public static final String FIRST_GIFT_CERTIFICATE_JSON_NODE_NAME = "giftCertificateJson1";
    public static final String SECOND_GIFT_CERTIFICATE_JSON_NODE_NAME = "giftCertificateJson2";
    public static final String GIFT_CERTIFICATE_PARAMETERS_JSON_NODE_NAME = "giftCertificateParameters";
    public static final String GIFT_CERTIFICATE_FOR_UPDATE_JSON_NODE_NAME = "giftCertificateForUpdateJson";
    public static final String TAG_JSON_NODE_NAME = "tagJson";
    public static final String GIFT_CERTIFICATE_FIELD_ID_KEY = "id";
    public static final String GIFT_CERTIFICATE_FIELD_NAME_KEY = "name";
    public static final String GIFT_CERTIFICATE_FIELD_DESCRIPTION_KEY = "description";
    public static final String GIFT_CERTIFICATE_FIELD_PRICE_KEY = "price";
    public static final String GIFT_CERTIFICATE_FIELD_DURATION_KEY = "duration";
    public static final String GIFT_CERTIFICATE_PARAMETERS_FIELD_PART_NAME_KEY = "part-name";
    public static final String TAG_FIELD_ID_KEY = "id";
    public static final String TAG_FIELD_NAME_KEY = "name";
}
