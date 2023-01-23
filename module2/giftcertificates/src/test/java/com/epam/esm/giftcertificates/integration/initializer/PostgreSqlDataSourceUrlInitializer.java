package com.epam.esm.giftcertificates.integration.initializer;

import com.epam.esm.giftcertificates.integration.container.IntegrationTestPostgreSqlContainer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class PostgreSqlDataSourceUrlInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(configurableApplicationContext,
                "jdbc.url=" + IntegrationTestPostgreSqlContainer.getInstance().getJdbcUrl());
    }
}
