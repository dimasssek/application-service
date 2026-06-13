package ru.kubsu.applicationservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.config.RabbitQueueProperties;
import ru.kubsu.applicationservice.service.ClientProjectionService;
import ru.kubsu.contracts.dto.service.client.ClientTo;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.amqp.support.AmqpHeaders.RECEIVED_ROUTING_KEY;

/**
 * Consumer событий клиента из {@code client.events.exchange}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ClientEventsListener {

    /** Сервис проекции клиента. */
    private final ClientProjectionService clientProjectionService;

    /** Свойства имён RabbitMQ. */
    private final RabbitQueueProperties rabbitQueueProperties;

    /** JSON-сериализатор. */
    private final JsonMapper jsonMapper;

    /**
     * Обрабатывает событие клиента из RabbitMQ.
     *
     * @param payload    JSON-тело сообщения ({@link ClientTo})
     * @param routingKey routing key сообщения
     */
    @RabbitListener(queues = "${app.rabbit.queue.client-events}")
    public void onClientEvent(String payload, @Header(RECEIVED_ROUTING_KEY) String routingKey) {
        validateRoutingKey(routingKey);
        ClientTo clientTo = jsonMapper.readValue(payload, ClientTo.class);
        log.debug("Получено событие клиента: id={}, routingKey={}", clientTo.getId(), routingKey);
        clientProjectionService.applyClientEvent(clientTo);
    }

    /**
     * Проверяет, что routing key относится к событиям клиента.
     *
     * @param routingKey routing key сообщения
     */
    private void validateRoutingKey(String routingKey) {
        if (!isClientEventRoutingKey(routingKey)) {
            throw new IllegalArgumentException("Неподдерживаемый routing key события клиента: " + routingKey);
        }
    }

    /**
     * Определяет, является ли routing key событием клиента.
     *
     * @param routingKey routing key сообщения
     * @return {@code true}, если routing key поддерживается
     */
    private boolean isClientEventRoutingKey(String routingKey) {
        return routingKey.equals(rabbitQueueProperties.getRoutingKey().getClientCreated())
                || routingKey.equals(rabbitQueueProperties.getRoutingKey().getClientUpdated())
                || routingKey.equals(rabbitQueueProperties.getRoutingKey().getClientDeleted());
    }
}
