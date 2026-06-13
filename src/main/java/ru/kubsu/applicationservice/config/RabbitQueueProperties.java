package ru.kubsu.applicationservice.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Свойства имён RabbitMQ exchanges, queues и routing keys.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.rabbit")
public class RabbitQueueProperties {

    /** Имена exchanges. */
    private Exchange exchange = new Exchange();

    /** Имена очередей. */
    private Queue queue = new Queue();

    /** Routing keys. */
    private RoutingKey routingKey = new RoutingKey();

    /**
     * Имена exchanges для RabbitMQ.
     */
    @Getter
    @Setter
    public static class Exchange {

        /** Exchange исходящих событий клиента. */
        @NotBlank
        private String clientEvents;

        /** Exchange исходящих событий заявлений для report-service. */
        @NotBlank
        private String applicationEvents;
    }

    /**
     * Имена очередей для RabbitMQ.
     */
    @Getter
    @Setter
    public static class Queue {

        /** Очередь событий клиента для application-service. */
        @NotBlank
        private String clientEvents;
    }

    /**
     * Routing keys для RabbitMQ.
     */
    @Getter
    @Setter
    public static class RoutingKey {

        /** Routing key события создания клиента. */
        @NotBlank
        private String clientCreated;

        /** Routing key события обновления клиента. */
        @NotBlank
        private String clientUpdated;

        /** Routing key события удаления клиента. */
        @NotBlank
        private String clientDeleted;

        /** Routing key события создания заявления. */
        @NotBlank
        private String applicationCreated;

        /** Routing key события обновления заявления. */
        @NotBlank
        private String applicationUpdated;

        /** Routing key события создания продуктового заявления. */
        @NotBlank
        private String applicationProductCreated;

        /** Routing key события обновления продуктового заявления. */
        @NotBlank
        private String applicationProductUpdated;

        /** Routing key события создания заявления на изменение профиля. */
        @NotBlank
        private String applicationProfileChangeCreated;

        /** Routing key события обновления заявления на изменение профиля. */
        @NotBlank
        private String applicationProfileChangeUpdated;

        /** Routing key события создания заявления на банковскую услугу. */
        @NotBlank
        private String applicationServiceCreated;

        /** Routing key события обновления заявления на банковскую услугу. */
        @NotBlank
        private String applicationServiceUpdated;

        /** Routing key события создания заявления на получение документа. */
        @NotBlank
        private String applicationDocumentCreated;

        /** Routing key события обновления заявления на получение документа. */
        @NotBlank
        private String applicationDocumentUpdated;

        /** Routing key события создания заявления-претензии. */
        @NotBlank
        private String applicationClaimCreated;

        /** Routing key события обновления заявления-претензии. */
        @NotBlank
        private String applicationClaimUpdated;

        /** Routing key события создания заявления на полномочия. */
        @NotBlank
        private String applicationAuthorityCreated;

        /** Routing key события обновления заявления на полномочия. */
        @NotBlank
        private String applicationAuthorityUpdated;
    }
}
