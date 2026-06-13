package ru.kubsu.applicationservice.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.config.OutboxProperties;
import ru.kubsu.applicationservice.entity.OutboxMessage;
import ru.kubsu.applicationservice.repository.OutboxMessageRepository;
import ru.kubsu.contracts.enums.service.application.OutboxStatus;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Обработка outbox-записей: публикация в RabbitMQ.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublishProcessor {

    private static final String PUBLISH_LOCATION = "OutboxPublishProcessor.publishLockedMessage";

    /** Репозиторий outbox-сообщений. */
    private final OutboxMessageRepository outboxMessageRepository;

    /** Шаблон публикации в RabbitMQ. */
    private final RabbitTemplate rabbitTemplate;

    /** Свойства outbox publisher. */
    private final OutboxProperties outboxProperties;

    /**
     * Публикует доступные outbox-записи со статусом NEW.
     */
    @Transactional
    public void publishPendingMessages() {
        List<OutboxMessage> messages = outboxMessageRepository.lockNextNewBatch(outboxProperties.getBatchSize());
        for (OutboxMessage message : messages) {
            publishLockedMessage(message);
        }
    }

    /**
     * Публикует одну заблокированную outbox-запись.
     *
     * @param message outbox-запись
     */
    private void publishLockedMessage(OutboxMessage message) {
        try {
            rabbitTemplate.convertAndSend(message.getExchangeName(), message.getRoutingKey(), message.getPayload());
            markSent(message);
        } catch (Exception exception) {
            handlePublishFailure(message, exception);
        }
    }

    /**
     * Помечает outbox-запись как успешно отправленную.
     *
     * @param message outbox-запись
     */
    private void markSent(OutboxMessage message) {
        message.setStatus(OutboxStatus.SENT);
        message.setSentAt(OffsetDateTime.now());
        message.setLastError(null);
        outboxMessageRepository.save(message);
    }

    /**
     * Обрабатывает ошибку публикации outbox-записи.
     *
     * @param message   outbox-запись
     * @param exception исключение
     */
    private void handlePublishFailure(OutboxMessage message, Exception exception) {
        int attempts = message.getAttempts() + 1;
        message.setAttempts(attempts);
        String errorMessage = OutboxErrorFormatter.format(
                PUBLISH_LOCATION, exception, outboxProperties.getMaxErrorLength());
        message.setLastError(errorMessage);

        if (attempts >= outboxProperties.getMaxAttempts()) {
            message.setStatus(OutboxStatus.FAILED);
            log.warn("Outbox-сообщение {} переведено в FAILED после {} попыток", message.getId(), attempts);
        }

        outboxMessageRepository.save(message);
    }
}
