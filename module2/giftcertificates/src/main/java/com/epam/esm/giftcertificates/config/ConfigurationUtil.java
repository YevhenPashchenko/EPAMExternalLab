package com.epam.esm.giftcertificates.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigurationUtil {

    public final String PARAMETER_NAME = "mediaType";
    public final String MEDIA_TYPE_EXTENSION = "json";
    public final String SERVLET_MAPPING = "/";
    public final String DRIVER_CLASS_NAME_PROPERTY_KEY = "jdbc.driver";
    public final String URL_PROPERTY_KEY = "jdbc.url";
    public final String USER_NAME_PROPERTY_KEY = "jdbc.username";
    public final String USER_PASSWORD_PROPERTY_KEY = "jdbc.password";
    public final String LIQUIBASE_CHANGELOG_MASTER_PATH = "classpath:db/db.changeLog-master.yml";
    public final String LIQUIBASE_DEFAULT_SCHEMA = "gc_schema";
}
