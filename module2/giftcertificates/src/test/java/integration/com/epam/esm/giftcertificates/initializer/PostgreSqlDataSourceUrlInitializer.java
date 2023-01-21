package integration.com.epam.esm.giftcertificates.initializer;

import integration.com.epam.esm.giftcertificates.container.IntegrationTestPostgreSqlContainer;
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
