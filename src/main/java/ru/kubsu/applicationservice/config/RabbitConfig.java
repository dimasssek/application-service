package ru.kubsu.applicationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ: exchanges, queues и bindings для consumer application-service.
 */
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    /** Свойства имён RabbitMQ. */
    private final RabbitQueueProperties rabbitQueueProperties;

    /**
     * Exchange исходящих событий клиента.
     *
     * @return topic exchange
     */
    @Bean
    public TopicExchange clientEventsExchange() {
        return new TopicExchange(rabbitQueueProperties.getExchange().getClientEvents());
    }

    /**
     * Exchange исходящих событий заявлений для report-service.
     *
     * @return topic exchange
     */
    @Bean
    public TopicExchange applicationEventsExchange() {
        return new TopicExchange(rabbitQueueProperties.getExchange().getApplicationEvents());
    }

    /**
     * Очередь событий клиента для application-service.
     *
     * @return durable queue
     */
    @Bean
    public Queue clientEventsQueue() {
        return new Queue(rabbitQueueProperties.getQueue().getClientEvents(), true);
    }

    /**
     * Binding очереди на событие создания клиента.
     *
     * @return binding
     */
    @Bean
    public Binding clientCreatedBinding() {
        return BindingBuilder
                .bind(clientEventsQueue())
                .to(clientEventsExchange())
                .with(rabbitQueueProperties.getRoutingKey().getClientCreated());
    }

    /**
     * Binding очереди на событие обновления клиента.
     *
     * @return binding
     */
    @Bean
    public Binding clientUpdatedBinding() {
        return BindingBuilder
                .bind(clientEventsQueue())
                .to(clientEventsExchange())
                .with(rabbitQueueProperties.getRoutingKey().getClientUpdated());
    }

    /**
     * Binding очереди на событие удаления клиента.
     *
     * @return binding
     */
    @Bean
    public Binding clientDeletedBinding() {
        return BindingBuilder
                .bind(clientEventsQueue())
                .to(clientEventsExchange())
                .with(rabbitQueueProperties.getRoutingKey().getClientDeleted());
    }
}
