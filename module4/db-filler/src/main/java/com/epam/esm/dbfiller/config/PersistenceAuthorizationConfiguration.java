package com.epam.esm.dbfiller.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
    basePackages = "com.epam.esm.dbfiller.repository.authorization",
    entityManagerFactoryRef = "authorizationEntityManager",
    transactionManagerRef = "authorizationTransactionManager"
)
public class PersistenceAuthorizationConfiguration {

    @Bean
    public PlatformTransactionManager authorizationTransactionManager() {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(authorizationEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean authorizationEntityManager() {
        var entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(authorizationDataSource());
        entityManager.setPackagesToScan("com.epam.esm.dbfiller.entity.authorization");

        var vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        return entityManager;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.authorization")
    public DataSource authorizationDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SpringLiquibase authorizationLiquibase() {
        var properties = authorizationLiquibaseProperties();
        var liquibase = new SpringLiquibase();
        liquibase.setDataSource(authorizationDataSource());
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.authorization.liquibase")
    public LiquibaseProperties authorizationLiquibaseProperties() {
        return new LiquibaseProperties();
    }
}
