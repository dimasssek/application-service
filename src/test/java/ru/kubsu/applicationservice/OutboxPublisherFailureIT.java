package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.kubsu.applicationservice.config.OutboxProperties;
import ru.kubsu.applicationservice.entity.OutboxMessage;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.outbox.OutboxPublishProcessor;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.repository.OutboxMessageRepository;
import ru.kubsu.applicationservice.support.ApplicationTestFixtures;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;
import ru.kubsu.contracts.enums.service.application.OutboxStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

/**
 * Интеграционные тесты outbox publisher при ошибках публикации.
 */
class OutboxPublisherFailureIT extends IntegrationTestBase {

    @Autowired
    private OutboxPublishProcessor outboxPublishProcessor;

    @Autowired
    private OutboxWriter outboxWriter;

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private OutboxProperties outboxProperties;

    @MockitoBean
    private RabbitTemplate rabbitTemplate;

    /**
     * Проверяет увеличение attempts при ошибке публикации без перевода в FAILED.
     */
    @Test
    void shouldIncrementAttemptsWhenPublishFails() {
        UUID applicationId = createApplicationOutboxMessage();
        doThrow(new RuntimeException("RabbitMQ unavailable"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        outboxPublishProcessor.publishPendingMessages();

        OutboxMessage message = findOutbox(applicationId, OutboxEventType.APPLICATION_CREATED);
        assertThat(message.getStatus()).isEqualTo(OutboxStatus.NEW);
        assertThat(message.getAttempts()).isEqualTo(1);
        assertThat(message.getLastError()).contains("RuntimeException: RabbitMQ unavailable");
        assertThat(message.getLastError()).contains("OutboxPublishProcessor.publishLockedMessage");
    }

    /**
     * Проверяет перевод outbox в FAILED после исчерпания попыток.
     */
    @Test
    void shouldMarkOutboxAsFailedWhenMaxAttemptsExceeded() {
        UUID applicationId = createApplicationOutboxMessage();
        doThrow(new RuntimeException("RabbitMQ unavailable"))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), anyString());

        for (int attempt = 0; attempt < outboxProperties.getMaxAttempts(); attempt++) {
            outboxPublishProcessor.publishPendingMessages();
        }

        OutboxMessage message = findOutbox(applicationId, OutboxEventType.APPLICATION_CREATED);
        assertThat(message.getStatus()).isEqualTo(OutboxStatus.FAILED);
        assertThat(message.getAttempts()).isEqualTo(outboxProperties.getMaxAttempts());
        assertThat(message.getLastError()).isNotBlank();
    }

    private UUID createApplicationOutboxMessage() {
        ProductApplication application = applicationRepository.save(ApplicationTestFixtures.validProductApplication());
        outboxWriter.writeApplicationReport(application, OutboxEventType.APPLICATION_CREATED);
        return application.getId();
    }

    private OutboxMessage findOutbox(UUID aggregateId, OutboxEventType eventType) {
        return outboxMessageRepository.findAll().stream()
                .filter(message -> aggregateId.equals(message.getAggregateId()))
                .filter(message -> eventType == message.getEventType())
                .findFirst()
                .orElseThrow();
    }
}
