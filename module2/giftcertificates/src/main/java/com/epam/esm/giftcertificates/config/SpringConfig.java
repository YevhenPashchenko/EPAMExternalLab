package com.epam.esm.giftcertificates.config;

import com.epam.esm.giftcertificates.constant.ConfigurationConstants;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
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
public class  SpringConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(true)
                .parameterName(ConfigurationConstants.PARAMETER_NAME)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType(ConfigurationConstants.MEDIA_TYPE_EXTENSION, MediaType.APPLICATION_JSON);
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
    public SpringLiquibase liquibase() {
        var liquibase = new SpringLiquibase();
        liquibase.setChangeLog(ConfigurationConstants.LIQUIBASE_CHANGELOG_MASTER_PATH);
        liquibase.setDataSource(dataSource());
        liquibase.setDefaultSchema(ConfigurationConstants.DEFAULT_SCHEMA);
        return liquibase;
    }

    @Bean
    public DataSource dataSource() {
        var dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects
                .requireNonNull(environment.getProperty(ConfigurationConstants.DRIVER_CLASS_NAME_PROPERTY_KEY)));
        dataSource.setUrl(environment.getProperty(ConfigurationConstants.URL_PROPERTY_KEY));
        dataSource.setUsername(environment.getProperty(ConfigurationConstants.USER_NAME_PROPERTY_KEY));
        dataSource.setPassword(environment.getProperty(ConfigurationConstants.USER_PASSWORD_PROPERTY_KEY));
        return dataSource;
    }
}
