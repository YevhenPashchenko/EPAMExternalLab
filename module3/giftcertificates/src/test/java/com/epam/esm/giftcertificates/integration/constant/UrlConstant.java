package com.epam.esm.giftcertificates.integration.constant;

public class UrlConstant {

  public static final String CREATE_GIFT_CERTIFICATE_URL = "/gift-certificates";
  public static final String GET_ALL_GIFT_CERTIFICATE_DTO_URL = "/gift-certificates?page=0&size=2";
  public static final String GET_GIFT_CERTIFICATE_DTO_BY_ID_URL = "/gift-certificates/1";
  public static final String GET_ALL_GIFT_CERTIFICATE_DTO_BY_PARAMETERS_URL =
      "/gift-certificates/sort?page=0&size=2";
  public static final String UPDATE_GIFT_CERTIFICATE_URL = "/gift-certificates";
  public static final String DELETE_GIFT_CERTIFICATE_URL = "/gift-certificates/1";

  public static final String CREATE_ORDER_URL = "/users/1/orders";
  public static final String GET_ALL_ORDER_DTO_URL = "/users/1/orders?page=0&size=2";
  public static final String GET_ORDER_DTO_BY_ID = "/users/orders/1";

  public static final String CREATE_TAG_URL = "/tags";
  public static final String GET_ALL_TAG_DTO_URL = "/tags?page=0&size=2";
  public static final String GET_TAG_DTO_BY_ID_URL = "/tags/1";
  public static final String DELETE_TAG_URL = "/tags/1";

  public static final String GET_ALL_USER_DTO_URL = "/users?page=0&size=2";
  public static final String GET_USER_DTO_BY_ID_URL = "/users/1";
  public static final String GET_USER_DTO_BY_ID_NOT_EXIST_URL = "/users/2";
}
