package ru.kubsu.applicationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.OutboxMessage;
import ru.kubsu.applicationservice.repository.OutboxMessageRepository;
import ru.kubsu.applicationservice.service.OutboxMessagePersistenceService;

/**
 * Реализация сервиса персистентности outbox-сообщений.
 */
@Service
@RequiredArgsConstructor
public class OutboxMessagePersistenceServiceImpl implements OutboxMessagePersistenceService {

    /** Репозиторий outbox-сообщений. */
    private final OutboxMessageRepository outboxMessageRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public OutboxMessage save(OutboxMessage outboxMessage) {
        return outboxMessageRepository.save(outboxMessage);
    }
}
