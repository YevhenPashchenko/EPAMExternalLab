package com.epam.esm.giftcertificates.config;

import com.epam.esm.giftcertificates.constant.ConfigurationConstants;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
public class SchemaConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof DataSource dataSource) {
            try(var connection = dataSource.getConnection();
                var statement = connection.createStatement()) {
                statement.execute(ConfigurationConstants.CREATE_DEFAULT_SCHEMA);
            } catch (SQLException exception) {
                throw new IllegalArgumentException("Failed to create " + ConfigurationConstants.DEFAULT_SCHEMA + " schema");
            }
        }
        return bean;
    }
}
