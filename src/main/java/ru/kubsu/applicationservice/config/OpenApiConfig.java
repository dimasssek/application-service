package ru.kubsu.applicationservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI / Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Описание OpenAPI для application-service.
     *
     * @return конфигурация OpenAPI
     */
    @Bean
    public OpenAPI applicationServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("application-service API")
                        .description("REST API микросервиса обработки заявлений банка")
                        .version("v1"));
    }
}
