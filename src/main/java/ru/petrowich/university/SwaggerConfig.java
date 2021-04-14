package ru.petrowich.university;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Contact contact = new Contact()
                .email("petr.kreslin@gmail.ru")
                .url("https://git.foxminded.com.ua/petrowich/university")
                .name("Petr Kreslin");

        Info info = new Info()
                .title("University API")
                .version("1.0.0")
                .contact(contact);

        return new OpenAPI().info(info);
    }
}
