package com.task.wallet.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация сваггера для отображения информации о приложении в UI API-документации
 */
@Configuration
public class OpenApiConfig {

    /**
     * Заголовок API
     */
    @Value("${api-title}")
    private String apiTitle;

    /**
     * Версия API
     */
    @Value("${api-version}")
    private String apiVersion;

    /**
     * Описание API
     */
    @Value("${api-description}")
    private String apiDescription;

    /**
     * Создание объект OpenAPI с настроенной информацией о приложении.
     *
     * @return конфигурация сваггера.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiTitle)
                        .version(apiVersion)
                        .description(apiDescription));
    }

}
