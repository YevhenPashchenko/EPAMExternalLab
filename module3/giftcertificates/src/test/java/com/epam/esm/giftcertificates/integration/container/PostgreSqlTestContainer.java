package com.epam.esm.giftcertificates.integration.container;

import com.epam.esm.giftcertificates.integration.constant.DatabasePropertyConstant;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlTestContainer implements BeforeAllCallback {

  private static PostgreSQLContainer<?> postgreSQLContainer;

  public static PostgreSQLContainer<?> getInstance() {
    if (postgreSQLContainer == null) {
      postgreSQLContainer =
          new PostgreSQLContainer<>(DatabasePropertyConstant.POSTGRES_IMAGE_VERSION)
              .withDatabaseName(DatabasePropertyConstant.DATABASE_NAME)
              .withUsername(DatabasePropertyConstant.USER_NAME)
              .withPassword(DatabasePropertyConstant.USER_PASSWORD);
    }
    return postgreSQLContainer;
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    postgreSQLContainer = getInstance();
    postgreSQLContainer.start();
    System.setProperty("JDBC_URL", postgreSQLContainer.getJdbcUrl());
  }
}
