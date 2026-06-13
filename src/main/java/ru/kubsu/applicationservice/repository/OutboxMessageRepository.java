package ru.kubsu.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kubsu.applicationservice.entity.OutboxMessage;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий outbox-сообщений.
 */
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, UUID> {

    /**
     * Блокирует следующую пачку outbox-записей со статусом NEW.
     *
     * @param limit максимальное количество записей
     * @return заблокированные outbox-записи
     */
    @Query(value = """
            SELECT *
            FROM outbox_message
            WHERE status = 'NEW'
            ORDER BY created_at
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxMessage> lockNextNewBatch(@Param("limit") int limit);
}
