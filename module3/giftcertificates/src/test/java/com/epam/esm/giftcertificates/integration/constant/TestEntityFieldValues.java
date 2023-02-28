package com.epam.esm.giftcertificates.integration.constant;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class TestEntityFieldValues {

    public final String NAME = "name1";
    public final String DESCRIPTION = "description1";
    public final BigDecimal PRICE = BigDecimal.valueOf(250.5);
    public final int DURATION = 15;

    public final String SECOND_NAME = "name2";
    public final String SECOND_DESCRIPTION = "description2";
    public final BigDecimal SECOND_PRICE = BigDecimal.valueOf(100.5);
    public final int SECOND_DURATION = 5;

    public final String FIRST_TAG_NAME = "tagName1";
    public final String SECOND_TAG_NAME = "tagName2";
    public final String THIRD_TAG_NAME = "tagName3";

    public final String EMAIL = "person1@mail.com";
    public final String PASSWORD = "password";
}
