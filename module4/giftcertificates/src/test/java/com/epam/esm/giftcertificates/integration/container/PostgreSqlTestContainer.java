package com.epam.esm.giftcertificates.integration.container;

import com.epam.esm.giftcertificates.integration.constant.DatabaseProperties;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlTestContainer implements Extension, BeforeAllCallback, AfterAllCallback {

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DatabaseProperties.POSTGRES_IMAGE_VERSION)
        .withDatabaseName(DatabaseProperties.DATABASE_NAME)
        .withUsername(DatabaseProperties.NAME)
        .withPassword(DatabaseProperties.PASSWORD);

    @Override
    public void beforeAll(ExtensionContext context) {
        postgreSQLContainer.start();
        System.setProperty("JDBC_URL", postgreSQLContainer.getJdbcUrl());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        // Test container is closed automatically after tests.
    }
}
