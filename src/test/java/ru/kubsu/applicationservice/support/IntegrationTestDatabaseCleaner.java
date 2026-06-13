package ru.kubsu.applicationservice.support;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Очищает изменяемые тестами таблицы между интеграционными тестами.
 * Справочник {@code status_inform} не затрагивается — он наполняется Liquibase один раз.
 */
@Component
@RequiredArgsConstructor
public class IntegrationTestDatabaseCleaner {

    /** JDBC-шаблон для выполнения очистки вне транзакции теста. */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Удаляет данные заявлений, клиентов, outbox и истории general-check.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void clean() {
        jdbcTemplate.execute("""
                delete from outbox_message;
                delete from general_check;
                delete from product_application;
                delete from service_application;
                delete from profile_change_application;
                delete from document_application;
                delete from claim_application;
                delete from authority_application;
                delete from application;
                delete from client;
                """);
    }
}
