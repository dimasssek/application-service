package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kubsu.applicationservice.entity.OutboxMessage;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.outbox.OutboxPublishProcessor;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.repository.OutboxMessageRepository;
import ru.kubsu.applicationservice.support.ApplicationTestFixtures;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ClientChangeType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;
import ru.kubsu.contracts.enums.service.application.OutboxStatus;
import ru.kubsu.contracts.messaging.service.application.ApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.ProductApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.ProfileChangeApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.ServiceApplicationReportMessage;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционные тесты outbox publisher.
 */
class OutboxPublisherIT extends IntegrationTestBase {

    @Autowired
    private OutboxPublishProcessor outboxPublishProcessor;

    @Autowired
    private OutboxWriter outboxWriter;

    @Autowired
    private OutboxMessageRepository outboxMessageRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * Проверяет публикацию outbox-события продуктового заявления и перевод записи в SENT.
     */
    @Test
    void shouldPublishProductApplicationOutboxMessagesToRabbit() {
        ProductApplication application = applicationRepository.save(ApplicationTestFixtures.validProductApplication());

        outboxWriter.writeApplicationReport(application, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeProductApplicationReport(application, OutboxEventType.PRODUCT_APPLICATION_CREATED);

        outboxPublishProcessor.publishPendingMessages();

        OutboxMessage applicationMessage = findOutbox(application.getId(), OutboxEventType.APPLICATION_CREATED);
        assertThat(applicationMessage.getStatus()).isEqualTo(OutboxStatus.SENT);
        assertThat(applicationMessage.getSentAt()).isNotNull();
        assertThat(applicationMessage.getLastError()).isNull();

        OutboxMessage productMessage = findOutbox(application.getId(), OutboxEventType.PRODUCT_APPLICATION_CREATED);
        assertThat(productMessage.getStatus()).isEqualTo(OutboxStatus.SENT);
        assertThat(productMessage.getRoutingKey()).isEqualTo("application.product.created");

        ApplicationReportMessage applicationPayload = jsonMapper.readValue(
                applicationMessage.getPayload(), ApplicationReportMessage.class);
        assertThat(applicationPayload.getNumber()).isEqualTo("BSPS-PRO-TEST-1");
        assertThat(applicationPayload.isDeleted()).isFalse();
        assertThat(applicationPayload.getStatusInternal()).isEqualTo(ApplicationStatusInternal.IN_QUEUE);
        assertThat(applicationPayload.getLastName()).isEqualTo("Иванов");
        assertThat(applicationPayload.getReason()).isEqualTo("Ожидает обработки");

        ProductApplicationReportMessage productPayload = jsonMapper.readValue(
                productMessage.getPayload(), ProductApplicationReportMessage.class);
        assertThat(productPayload.getApplicationId()).isEqualTo(application.getId());
        assertThat(productPayload.getProductName()).isEqualTo("Потребительский кредит");
    }

    /**
     * Проверяет публикацию outbox-события заявления на изменение профиля.
     */
    @Test
    void shouldPublishProfileChangeApplicationOutboxMessagesToRabbit() {
        ProfileChangeApplication application = applicationRepository.save(
                ApplicationTestFixtures.validProfileChangeApplication());

        outboxWriter.writeApplicationReport(application, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeProfileChangeApplicationReport(
                application, OutboxEventType.PROFILE_CHANGE_APPLICATION_CREATED);

        outboxPublishProcessor.publishPendingMessages();

        OutboxMessage profileMessage = findOutbox(
                application.getId(), OutboxEventType.PROFILE_CHANGE_APPLICATION_CREATED);
        assertThat(profileMessage.getStatus()).isEqualTo(OutboxStatus.SENT);

        ProfileChangeApplicationReportMessage payload = jsonMapper.readValue(
                profileMessage.getPayload(), ProfileChangeApplicationReportMessage.class);
        assertThat(payload.getChangeType()).isEqualTo(ClientChangeType.ADDRESS);
        assertThat(payload.getNewValue()).isEqualTo("г. Москва, ул. Новая, 2");
    }

    /**
     * Проверяет публикацию outbox-события заявления на банковскую услугу.
     */
    @Test
    void shouldPublishServiceApplicationOutboxMessagesToRabbit() {
        ServiceApplication application = applicationRepository.save(ApplicationTestFixtures.validServiceApplication());

        outboxWriter.writeApplicationReport(application, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeServiceApplicationReport(application, OutboxEventType.SERVICE_APPLICATION_CREATED);

        outboxPublishProcessor.publishPendingMessages();

        OutboxMessage serviceMessage = findOutbox(application.getId(), OutboxEventType.SERVICE_APPLICATION_CREATED);
        assertThat(serviceMessage.getStatus()).isEqualTo(OutboxStatus.SENT);
        assertThat(serviceMessage.getRoutingKey()).isEqualTo("application.service.created");

        ServiceApplicationReportMessage payload = jsonMapper.readValue(
                serviceMessage.getPayload(), ServiceApplicationReportMessage.class);
        assertThat(payload.getApplicationId()).isEqualTo(application.getId());
        assertThat(payload.getServiceName()).isEqualTo("SMS-информирование");
    }

    /**
     * Проверяет публикацию soft delete через APPLICATION_UPDATED с deleted = true.
     */
    @Test
    void shouldPublishSoftDeleteViaApplicationUpdated() {
        ProductApplication application = applicationRepository.save(ApplicationTestFixtures.validProductApplication());
        application.setDeleted(true);
        applicationRepository.save(application);

        outboxWriter.writeApplicationReport(application, OutboxEventType.APPLICATION_UPDATED);

        outboxPublishProcessor.publishPendingMessages();

        OutboxMessage message = findOutbox(application.getId(), OutboxEventType.APPLICATION_UPDATED);
        assertThat(message.getStatus()).isEqualTo(OutboxStatus.SENT);
        assertThat(message.getRoutingKey()).isEqualTo("application.updated");

        ApplicationReportMessage payload = jsonMapper.readValue(message.getPayload(), ApplicationReportMessage.class);
        assertThat(payload.isDeleted()).isTrue();
    }

    /**
     * Проверяет, что outbox-запись не публикуется повторно после успешной отправки.
     */
    @Test
    void shouldNotRepublishAlreadySentOutboxMessage() {
        ProductApplication application = applicationRepository.save(ApplicationTestFixtures.validProductApplication());
        outboxWriter.writeApplicationReport(application, OutboxEventType.APPLICATION_CREATED);

        outboxPublishProcessor.publishPendingMessages();
        long sentCount = outboxMessageRepository.findAll().stream()
                .filter(message -> message.getStatus() == OutboxStatus.SENT)
                .count();

        outboxPublishProcessor.publishPendingMessages();

        assertThat(outboxMessageRepository.findAll().stream()
                .filter(message -> message.getStatus() == OutboxStatus.SENT)
                .count()).isEqualTo(sentCount);
    }

    private OutboxMessage findOutbox(UUID aggregateId, OutboxEventType eventType) {
        return outboxMessageRepository.findAll().stream()
                .filter(message -> aggregateId.equals(message.getAggregateId()))
                .filter(message -> eventType == message.getEventType())
                .findFirst()
                .orElseThrow();
    }
}
