package ru.kubsu.applicationservice.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Feign-клиентов к смежным микросервисам.
 */
@Configuration
@EnableFeignClients(basePackages = "ru.kubsu.applicationservice.integration")
@ConfigurationPropertiesScan(basePackages = "ru.kubsu.applicationservice.config")
public class FeignConfig {
}
