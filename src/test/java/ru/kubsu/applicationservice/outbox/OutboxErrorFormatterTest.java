package ru.kubsu.applicationservice.outbox;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-тесты форматирования ошибок outbox.
 */
class OutboxErrorFormatterTest {

    /**
     * Проверяет формат ошибки без stacktrace.
     */
    @Test
    void shouldFormatErrorWithLocationAndMessage() {
        String formatted = OutboxErrorFormatter.format(
                "OutboxPublishProcessor.publishLockedMessage",
                new IllegalStateException("broker down"),
                2000);

        assertThat(formatted).isEqualTo(
                "IllegalStateException: broker down at OutboxPublishProcessor.publishLockedMessage");
    }

    /**
     * Проверяет обрезку слишком длинного текста ошибки.
     */
    @Test
    void shouldTruncateErrorWhenExceedsMaxLength() {
        String formatted = OutboxErrorFormatter.format(
                "location",
                new RuntimeException("x".repeat(100)),
                50);

        assertThat(formatted).hasSize(50);
    }
}
