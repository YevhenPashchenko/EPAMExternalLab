package com.epam.esm.giftcertificates.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigurationConstants {

    public final String PARAMETER_NAME = "mediaType";
    public final String MEDIA_TYPE_EXTENSION = "json";
    public final String SERVLET_MAPPING = "/";
    public final String DRIVER_CLASS_NAME_PROPERTY_KEY = "jdbc.driver";
    public final String URL_PROPERTY_KEY = "jdbc.url";
    public final String USER_NAME_PROPERTY_KEY = "jdbc.username";
    public final String USER_PASSWORD_PROPERTY_KEY = "jdbc.password";
    public final String LIQUIBASE_CHANGELOG_MASTER_PATH = "classpath:db/db.changeLog-master.yml";
    public final String CREATE_DEFAULT_SCHEMA = "CREATE SCHEMA IF NOT EXISTS " + ConfigurationConstants.DEFAULT_SCHEMA +
            " AUTHORIZATION postgres; GRANT USAGE ON SCHEMA " + ConfigurationConstants.DEFAULT_SCHEMA + " TO PUBLIC; " +
            "GRANT ALL ON SCHEMA " + ConfigurationConstants.DEFAULT_SCHEMA + " TO postgres;";
    public final String DEFAULT_SCHEMA = "gift_certificates";
}
