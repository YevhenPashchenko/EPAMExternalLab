package com.epam.esm.giftcertificates.config;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("com.epam.esm.giftcertificates")
@PropertySource("classpath:jdbc.properties")
@EnableWebMvc
@AllArgsConstructor
public class SpringConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        String parameterName = "mediaType";
        String extension = "json";
        MediaType jsonMediaType = MediaType.APPLICATION_JSON;

        configurer.favorParameter(true)
                .parameterName(parameterName)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(jsonMediaType)
                .mediaType(extension, jsonMediaType);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        String driverClassName = Objects.requireNonNull(environment.getProperty("jdbc.driver"));
        String url = environment.getProperty("jdbc.url");
        String userName = environment.getProperty("jdbc.username");
        String userPassword = environment.getProperty("jdbc.password");

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(userPassword);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
