package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kubsu.applicationservice.repository.StatusInformRepository;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет накат Liquibase-миграций и соответствие схемы БД доменной модели.
 */
class SchemaValidationIT extends IntegrationTestBase {

    private static final Set<String> EXPECTED_TABLES = Set.of(
            "client",
            "application",
            "product_application",
            "service_application",
            "profile_change_application",
            "document_application",
            "claim_application",
            "authority_application",
            "status_inform",
            "outbox_message",
            "general_check"
    );

    @Autowired
    private DataSource dataSource;

    @Autowired
    private StatusInformRepository statusInformRepository;

    /**
     * Все таблицы доменной модели должны существовать после миграций.
     */
    @Test
    void shouldCreateAllDomainTablesWhenLiquibaseMigrationApplied() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("""
                     select table_name
                     from information_schema.tables
                     where table_schema = 'public'
                       and table_type = 'BASE TABLE'
                     """)) {

            Set<String> actualTables = collectTableNames(resultSet);

            assertThat(actualTables).containsAll(EXPECTED_TABLES);
        }
    }

    /**
     * Матрица status_inform должна быть заполнена процедурой refill_status_inform.
     */
    @Test
    void shouldRefillStatusInformWhenLiquibaseMigrationApplied() {
        assertThat(statusInformRepository.count()).isEqualTo(11);
        assertThat(statusInformRepository.findByStatusInternal(ApplicationStatusInternal.IN_QUEUE))
                .isPresent()
                .get()
                .extracting("reason")
                .isEqualTo("Ожидает обработки");
    }

    private Set<String> collectTableNames(ResultSet resultSet) throws Exception {
        Set<String> tables = new HashSet<>();
        while (resultSet.next()) {
            tables.add(resultSet.getString("table_name"));
        }
        return tables;
    }
}
