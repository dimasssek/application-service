package ru.kubsu.applicationservice.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Генератор номера заявления на основе PostgreSQL sequence.
 */
@Service
public class ApplicationNumberGeneratorImpl implements ApplicationNumberGenerator {

    private static final String NUMBER_PREFIX = "BSPS-";

    private final JdbcTemplate jdbcTemplate;

    /**
     * Создаёт генератор номеров заявлений.
     *
     * @param jdbcTemplate JDBC-шаблон для доступа к sequence
     */
    public ApplicationNumberGeneratorImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nextNumber(ApplicationType applicationType) {
        String sql = "select nextval('" + applicationType.getSequenceName() + "')";
        Long sequenceValue = jdbcTemplate.queryForObject(sql, Long.class);
        return NUMBER_PREFIX + applicationType.getNumberCode() + "-" + sequenceValue;
    }
}
