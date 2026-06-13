package ru.kubsu.applicationservice.service;

import ru.kubsu.applicationservice.entity.OutboxMessage;

/**
 * Сервис персистентности outbox-сообщений.
 */
public interface OutboxMessagePersistenceService {

    /**
     * Сохраняет outbox-сообщение в базе данных.
     *
     * @param outboxMessage сущность outbox-сообщения
     * @return сохранённое outbox-сообщение
     */
    OutboxMessage save(OutboxMessage outboxMessage);
}
