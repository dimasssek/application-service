package ru.kubsu.applicationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа микросервиса обработки банковских заявлений.
 */
@SpringBootApplication
public class Application {

    /**
     * Запускает Spring Boot-приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
