package ru.petrowich.university;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class AppConfiguration {
    @Bean(name = "validator")
    public Validator validator() {
        return buildDefaultValidatorFactory().getValidator();
    }

    @Bean (name = "modelMapper")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        return modelMapper;
    }
}
