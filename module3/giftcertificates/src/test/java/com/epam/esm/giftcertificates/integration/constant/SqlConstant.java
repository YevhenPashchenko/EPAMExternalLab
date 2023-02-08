package com.epam.esm.giftcertificates.integration.constant;

public class SqlConstant {

  public static final String TRUNCATE_TABLES =
      "TRUNCATE TABLE gift_certificate, \"order\", tag, \"user\" CASCADE";
  public static final String RESTART_GIFT_CERTIFICATE_ID_SEQUENCE =
      "ALTER SEQUENCE gift_certificate_id_seq RESTART";
  public static final String RESTART_ORDER_ID_SEQUENCE = "ALTER SEQUENCE order_id_seq RESTART";
  public static final String RESTART_TAG_ID_SEQUENCE = "ALTER SEQUENCE tag_id_seq RESTART";
  public static final String RESTART_USER_ID_SEQUENCE = "ALTER SEQUENCE user_id_seq RESTART";
  public static final String CREATE_USER =
      "INSERT INTO \"user\" VALUES (nextval('user_id_seq'), 'user1@mail.com', 'password', "
          + "'2023-02-02 18:55:12.579333', '2023-02-02 18:55:12.579333')";
  public static final String UPDATE_ORDER_CREATE_DATE =
      "UPDATE \"user\" SET create_date = '2023-02-08T14:35:09.2310829' WHERE id = 1";
}
