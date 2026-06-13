package ru.kubsu.applicationservice.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Свойства пакетной обработки general-check.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.general-check")
public class GeneralCheckProperties {

    /** Размер пула потоков для параллельной обработки заявлений. */
    @Min(1)
    private int poolSize = 4;

    /** Таймаут ожидания завершения пула потоков в секундах. */
    @Min(1)
    private long awaitTerminationSeconds = 300;
}
