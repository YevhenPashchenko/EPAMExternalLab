package com.epam.esm.giftcertificates.integration.container;

import com.epam.esm.giftcertificates.integration.constant.IntegrationTestConstant;
import org.testcontainers.containers.PostgreSQLContainer;

public class IntegrationTestPostgreSqlContainer extends PostgreSQLContainer<IntegrationTestPostgreSqlContainer> {

    private static IntegrationTestPostgreSqlContainer container;

    private IntegrationTestPostgreSqlContainer() {
        super(IntegrationTestConstant.POSTGRES_IMAGE_VERSION);
    }

    public static IntegrationTestPostgreSqlContainer getInstance() {
        if (container == null) {
            container = new IntegrationTestPostgreSqlContainer()
                    .withDatabaseName(IntegrationTestConstant.DATABASE_NAME)
                    .withUsername(IntegrationTestConstant.USER_NAME)
                    .withPassword(IntegrationTestConstant.USER_PASSWORD);
        }
        return container;
    }
}
