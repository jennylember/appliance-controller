package ru.lember.appliancecontroller.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lember.appliancecontroller.properties.BusinessProperties;


@Configuration
public class ApplicationConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "business-properties")
    public BusinessProperties businessProperties() {
        return new BusinessProperties.Bean();
    }

}
