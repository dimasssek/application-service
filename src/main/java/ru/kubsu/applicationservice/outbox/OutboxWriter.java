package ru.kubsu.applicationservice.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.config.RabbitQueueProperties;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.entity.OutboxMessage;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.mapper.ReportMessageMapper;
import ru.kubsu.applicationservice.service.OutboxMessagePersistenceService;
import ru.kubsu.contracts.enums.service.application.AggregateType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;
import ru.kubsu.contracts.enums.service.application.OutboxStatus;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Запись outbox-сообщений в рамках бизнес-транзакции.
 */
@Component
@RequiredArgsConstructor
public class OutboxWriter {

    /** Сервис персистентности outbox-сообщений. */
    private final OutboxMessagePersistenceService outboxMessagePersistenceService;

    /** Свойства имён RabbitMQ. */
    private final RabbitQueueProperties rabbitQueueProperties;

    /** Маппер report-сообщений. */
    private final ReportMessageMapper reportMessageMapper;

    /** JSON-сериализатор. */
    private final JsonMapper jsonMapper;

    /**
     * Сохраняет outbox-событие по заявлению.
     *
     * @param application заявление
     * @param eventType   тип события
     */
    public void writeApplicationReport(ApplicationEntity application, OutboxEventType eventType) {
        saveOutboxMessage(
                AggregateType.APPLICATION,
                application.getId(),
                eventType,
                jsonMapper.writeValueAsString(reportMessageMapper.toApplicationReportMessage(application)),
                resolveRoutingKey(eventType));
    }

    /**
     * Сохраняет outbox-событие по продуктовому заявлению.
     *
     * @param application продуктовое заявление
     * @param eventType   тип события
     */
    public void writeProductApplicationReport(ProductApplication application, OutboxEventType eventType) {
        saveOutboxMessage(
                AggregateType.PRODUCT_APPLICATION,
                application.getId(),
                eventType,
                jsonMapper.writeValueAsString(reportMessageMapper.toProductApplicationReportMessage(application)),
                resolveRoutingKey(eventType));
    }

    /**
     * Сохраняет outbox-событие по заявлению на изменение профиля.
     *
     * @param application заявление на изменение профиля
     * @param eventType   тип события
     */
    public void writeProfileChangeApplicationReport(ProfileChangeApplication application, OutboxEventType eventType) {
        saveOutboxMessage(
                AggregateType.PROFILE_CHANGE_APPLICATION,
                application.getId(),
                eventType,
                jsonMapper.writeValueAsString(reportMessageMapper.toProfileChangeApplicationReportMessage(application)),
                resolveRoutingKey(eventType));
    }

    /**
     * Сохраняет outbox-событие по заявлению на банковскую услугу.
     *
     * @param application заявление на банковскую услугу
     * @param eventType   тип события
     */
    public void writeServiceApplicationReport(ServiceApplication application, OutboxEventType eventType) {
        saveOutboxMessage(
                AggregateType.SERVICE_APPLICATION,
                application.getId(),
                eventType,
                jsonMapper.writeValueAsString(reportMessageMapper.toServiceApplicationReportMessage(application)),
                resolveRoutingKey(eventType));
    }

    /**
     * Сохраняет outbox-событие по заявлению на получение документа.
     *
     * @param application заявление на получение документа
     * @param eventType   тип события
     */
    public void writeDocumentApplicationReport(DocumentApplication application, OutboxEventType eventType) {
        saveOutboxMessage(
                AggregateType.DOCUMENT_APPLICATION,
                application.getId(),
                eventType,
                jsonMapper.writeValueAsString(reportMessageMapper.toDocumentApplicationReportMessage(application)),
                resolveRoutingKey(eventType));
    }

    /**
     * Сохраняет outbox-событие по заявлению-претензии.
     *
     * @param application заявление-претензия
     * @param eventType   тип события
     */
    public void writeClaimApplicationReport(ClaimApplication application, OutboxEventType eventType) {
        saveOutboxMessage(
                AggregateType.CLAIM_APPLICATION,
                application.getId(),
                eventType,
                jsonMapper.writeValueAsString(reportMessageMapper.toClaimApplicationReportMessage(application)),
                resolveRoutingKey(eventType));
    }

    /**
     * Сохраняет outbox-событие по заявлению на полномочия.
     *
     * @param application заявление на полномочия
     * @param eventType   тип события
     */
    public void writeAuthorityApplicationReport(AuthorityApplication application, OutboxEventType eventType) {
        saveOutboxMessage(
                AggregateType.AUTHORITY_APPLICATION,
                application.getId(),
                eventType,
                jsonMapper.writeValueAsString(reportMessageMapper.toAuthorityApplicationReportMessage(application)),
                resolveRoutingKey(eventType));
    }

    /**
     * Сохраняет outbox-запись в базе данных.
     *
     * @param aggregateType тип агрегата
     * @param aggregateId   идентификатор агрегата
     * @param eventType     тип события
     * @param payload       JSON-тело сообщения
     * @param routingKey    routing key
     */
    private void saveOutboxMessage(AggregateType aggregateType,
                                   UUID aggregateId,
                                   OutboxEventType eventType,
                                   String payload,
                                   String routingKey) {
        OutboxMessage outboxMessage = new OutboxMessage()
                .setAggregateType(aggregateType)
                .setAggregateId(aggregateId)
                .setEventType(eventType)
                .setPayload(payload)
                .setExchangeName(rabbitQueueProperties.getExchange().getApplicationEvents())
                .setRoutingKey(routingKey)
                .setStatus(OutboxStatus.NEW)
                .setAttempts(0)
                .setCreatedAt(OffsetDateTime.now());

        outboxMessagePersistenceService.save(outboxMessage);
    }

    /**
     * Определяет routing key по типу события.
     *
     * @param eventType тип события
     * @return routing key
     */
    private String resolveRoutingKey(OutboxEventType eventType) {
        return switch (eventType) {
            case APPLICATION_CREATED -> rabbitQueueProperties.getRoutingKey().getApplicationCreated();
            case APPLICATION_UPDATED -> rabbitQueueProperties.getRoutingKey().getApplicationUpdated();
            case PRODUCT_APPLICATION_CREATED -> rabbitQueueProperties.getRoutingKey().getApplicationProductCreated();
            case PRODUCT_APPLICATION_UPDATED -> rabbitQueueProperties.getRoutingKey().getApplicationProductUpdated();
            case PROFILE_CHANGE_APPLICATION_CREATED ->
                    rabbitQueueProperties.getRoutingKey().getApplicationProfileChangeCreated();
            case PROFILE_CHANGE_APPLICATION_UPDATED ->
                    rabbitQueueProperties.getRoutingKey().getApplicationProfileChangeUpdated();
            case SERVICE_APPLICATION_CREATED -> rabbitQueueProperties.getRoutingKey().getApplicationServiceCreated();
            case SERVICE_APPLICATION_UPDATED -> rabbitQueueProperties.getRoutingKey().getApplicationServiceUpdated();
            case DOCUMENT_APPLICATION_CREATED -> rabbitQueueProperties.getRoutingKey().getApplicationDocumentCreated();
            case DOCUMENT_APPLICATION_UPDATED -> rabbitQueueProperties.getRoutingKey().getApplicationDocumentUpdated();
            case CLAIM_APPLICATION_CREATED -> rabbitQueueProperties.getRoutingKey().getApplicationClaimCreated();
            case CLAIM_APPLICATION_UPDATED -> rabbitQueueProperties.getRoutingKey().getApplicationClaimUpdated();
            case AUTHORITY_APPLICATION_CREATED -> rabbitQueueProperties.getRoutingKey().getApplicationAuthorityCreated();
            case AUTHORITY_APPLICATION_UPDATED -> rabbitQueueProperties.getRoutingKey().getApplicationAuthorityUpdated();
        };
    }
}
