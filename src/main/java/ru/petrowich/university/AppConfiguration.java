package ru.petrowich.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;

@Configuration
public class AppConfiguration {
    @Bean(name = "validator")
    public Validator validator() {
        return buildDefaultValidatorFactory().getValidator();
    }
}
