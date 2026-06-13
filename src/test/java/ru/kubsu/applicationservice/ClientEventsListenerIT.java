package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.config.RabbitQueueProperties;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.applicationservice.repository.ClientRepository;
import ru.kubsu.applicationservice.support.ClientTestFixtures;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.contracts.dto.service.client.ClientTo;
import ru.kubsu.contracts.enums.service.client.ClientStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет end-to-end доставку событий клиента через RabbitMQ.
 */
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class ClientEventsListenerIT extends IntegrationTestBase {

    private static final long WAIT_TIMEOUT_MS = 5000L;
    private static final long POLL_INTERVAL_MS = 100L;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitQueueProperties rabbitQueueProperties;

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Сообщение, опубликованное в exchange, должно спроецироваться в витрину client.
     */
    @Test
    void shouldProjectClientWhenMessagePublishedToExchange() {
        OffsetDateTime actualDate = OffsetDateTime.parse("2026-05-31T10:00:00Z");
        ClientTo event = ClientTestFixtures.clientEvent(actualDate);
        String payload = jsonMapper.writeValueAsString(event);

        rabbitTemplate.convertAndSend(
                rabbitQueueProperties.getExchange().getClientEvents(),
                rabbitQueueProperties.getRoutingKey().getClientCreated(),
                payload);

        waitUntilClientExists(event.getId());

        Client savedClient = clientRepository.findById(event.getId()).orElseThrow();
        assertThat(savedClient.getLastName()).isEqualTo("Иванов");
        assertThat(savedClient.getStatus()).isEqualTo(ClientStatus.ACTUAL);
    }

    /**
     * Событие удаления через RabbitMQ должно помечать клиента как DELETED.
     */
    @Test
    void shouldMarkClientDeletedWhenDeletedMessagePublishedToExchange() {
        OffsetDateTime createdDate = OffsetDateTime.parse("2026-05-31T10:00:00Z");
        OffsetDateTime deletedDate = OffsetDateTime.parse("2026-05-31T12:00:00Z");
        ClientTo createdEvent = ClientTestFixtures.clientEvent(createdDate);
        ClientTo deletedEvent = ClientTestFixtures.deletedClientEvent(createdEvent, deletedDate);

        rabbitTemplate.convertAndSend(
                rabbitQueueProperties.getExchange().getClientEvents(),
                rabbitQueueProperties.getRoutingKey().getClientCreated(),
                jsonMapper.writeValueAsString(createdEvent));

        waitUntilClientExists(createdEvent.getId());

        rabbitTemplate.convertAndSend(
                rabbitQueueProperties.getExchange().getClientEvents(),
                rabbitQueueProperties.getRoutingKey().getClientDeleted(),
                jsonMapper.writeValueAsString(deletedEvent));

        waitUntilClientStatus(deletedEvent.getId(), ClientStatus.DELETED);
    }

    /**
     * Ожидает появления клиента в витрине после обработки сообщения.
     *
     * @param clientId идентификатор клиента
     */
    private void waitUntilClientExists(UUID clientId) {
        long deadline = System.currentTimeMillis() + WAIT_TIMEOUT_MS;
        while (System.currentTimeMillis() < deadline) {
            if (clientRepository.findById(clientId).isPresent()) {
                return;
            }
            sleepQuietly();
        }
        assertThat(clientRepository.findById(clientId)).isPresent();
    }

    /**
     * Ожидает нужного статуса клиента в витрине.
     *
     * @param clientId идентификатор клиента
     * @param status   ожидаемый статус
     */
    private void waitUntilClientStatus(UUID clientId, ClientStatus status) {
        long deadline = System.currentTimeMillis() + WAIT_TIMEOUT_MS;
        while (System.currentTimeMillis() < deadline) {
            if (clientRepository.findById(clientId)
                    .map(client -> client.getStatus() == status)
                    .orElse(false)) {
                return;
            }
            sleepQuietly();
        }
        assertThat(clientRepository.findById(clientId))
                .isPresent()
                .get()
                .extracting(Client::getStatus)
                .isEqualTo(status);
    }

    /**
     * Пауза между poll-проверками состояния витрины.
     */
    private void sleepQuietly() {
        try {
            Thread.sleep(POLL_INTERVAL_MS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Прервано ожидание обработки события клиента", exception);
        }
    }
}
