package com.epam.esm.giftcertificates.config;

import com.epam.esm.giftcertificates.constant.ConfigurationConstants;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class SpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                SpringConfig.class
        };
    }

    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[] {
                ConfigurationConstants.SERVLET_MAPPING
        };
    }
}
