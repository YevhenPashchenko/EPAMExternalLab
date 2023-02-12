package com.epam.esm.giftcertificates.integration.constant;

public class UrlConstant {

  public static final String CREATE_GIFT_CERTIFICATE_URL = "/gift-certificates";
  public static final String GET_ALL_GIFT_CERTIFICATES = "/gift-certificates?page=0&size=2";
  public static final String GET_GIFT_CERTIFICATE_BY_ID_URL = "/gift-certificates/1";
  public static final String GET_ALL_GIFT_CERTIFICATES_BY_PARAMETERS_URL =
      "/gift-certificates/sort?page=0&size=2";
  public static final String UPDATE_GIFT_CERTIFICATE_URL = "/gift-certificates";
  public static final String DELETE_GIFT_CERTIFICATE_URL = "/gift-certificates/1";

  public static final String CREATE_RECIPE_URL = "/persons/1/recipes";
  public static final String GET_ALL_RECIPES_URL = "/persons/1/recipes?page=0&size=2";
  public static final String GET_RECIPE_BY_ID = "/persons/recipes/1";

  public static final String CREATE_TAG_URL = "/tags";
  public static final String GET_ALL_TAGS_URL = "/tags?page=0&size=2";
  public static final String GET_TAG_BY_ID_URL = "/tags/1";
  public static final String GET_MOST_WIDELY_USED_TAGS = "/tags/persons/1?page=0&size=2";
  public static final String DELETE_TAG_URL = "/tags/1";

  public static final String GET_ALL_PERSONS_URL = "/persons?page=0&size=2";
  public static final String GET_PERSON_BY_ID_URL = "/persons/1";
  public static final String GET_PERSON_BY_ID_NOT_EXIST_URL = "/persons/2";
}
