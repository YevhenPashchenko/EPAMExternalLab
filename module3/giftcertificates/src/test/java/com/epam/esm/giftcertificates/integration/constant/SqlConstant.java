package com.epam.esm.giftcertificates.integration.constant;

public class SqlConstant {

  public static final String TRUNCATE_TABLES =
      "TRUNCATE TABLE gift_certificate, recipe, tag, person CASCADE";
  public static final String RESTART_GIFT_CERTIFICATE_ID_SEQUENCE =
      "ALTER SEQUENCE gift_certificate_id_seq RESTART";
  public static final String RESTART_RECIPE_ID_SEQUENCE = "ALTER SEQUENCE recipe_id_seq RESTART";
  public static final String RESTART_TAG_ID_SEQUENCE = "ALTER SEQUENCE tag_id_seq RESTART";
  public static final String RESTART_PERSON_ID_SEQUENCE = "ALTER SEQUENCE person_id_seq RESTART";
  public static final String CREATE_PERSON =
      "INSERT INTO person VALUES (nextval('person_id_seq'), 'user1@mail.com', 'password', "
          + "'2023-02-02 18:55:12.579333', '2023-02-02 18:55:12.579333')";
}
