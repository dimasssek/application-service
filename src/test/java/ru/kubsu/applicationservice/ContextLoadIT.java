package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import ru.kubsu.applicationservice.support.IntegrationTestBase;

/**
 * Проверяет успешную инициализацию Spring-контекста с доменной моделью и Liquibase-миграциями.
 */
class ContextLoadIT extends IntegrationTestBase {

    /**
     * Контекст должен подниматься без ошибок при доступной инфраструктуре Testcontainers.
     */
    @Test
    void shouldLoadApplicationContextWhenInfrastructureIsAvailable() {
    }
}
