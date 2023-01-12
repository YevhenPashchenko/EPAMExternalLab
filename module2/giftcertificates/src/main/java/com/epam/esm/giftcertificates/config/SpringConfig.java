package com.epam.esm.giftcertificates.config;

import com.epam.esm.giftcertificates.dao.GiftCertificateQueryBuilder;
import com.epam.esm.giftcertificates.dao.impl.GiftCertificateQueryBuilderImpl;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("com.epam.esm.giftcertificates")
@PropertySource("classpath:jdbc.properties")
@EnableWebMvc
@EnableTransactionManagement
@RequiredArgsConstructor
public class SpringConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true)
                .parameterName(ConfigurationUtil.PARAMETER_NAME)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType(ConfigurationUtil.MEDIA_TYPE_EXTENSION, MediaType.APPLICATION_JSON);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(Objects
                .requireNonNull(environment.getProperty(ConfigurationUtil.DRIVER_CLASS_NAME_PROPERTY_KEY)));
        dataSource.setUrl(environment.getProperty(ConfigurationUtil.URL_PROPERTY_KEY));
        dataSource.setUsername(environment.getProperty(ConfigurationUtil.USER_NAME_PROPERTY_KEY));
        dataSource.setPassword(environment.getProperty(ConfigurationUtil.USER_PASSWORD_PROPERTY_KEY));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @RequestScope
    public GiftCertificateQueryBuilder giftCertificateQueryBuilder() {
        return new GiftCertificateQueryBuilderImpl();
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setChangeLog(ConfigurationUtil.LIQUIBASE_CHANGELOG_MASTER_PATH);
        liquibase.setDataSource(dataSource());
        liquibase.setDefaultSchema(ConfigurationUtil.LIQUIBASE_DEFAULT_SCHEMA);

        return liquibase;
    }
}
