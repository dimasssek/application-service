package ru.kubsu.applicationservice.usecase.base;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

import java.util.UUID;

/**
 * Базовый сценарий мягкого удаления заявления.
 *
 * @param <E> тип сущности заявления
 */
@RequiredArgsConstructor
public abstract class BaseDeleteUseCase<E extends ApplicationEntity> {

    /** Репозиторий заявлений. */
    protected final ApplicationRepository applicationRepository;

    /** Сервис доступа к заявлениям. */
    protected final ApplicationAccessService applicationAccessService;

    /** Запись outbox-сообщений. */
    protected final OutboxWriter outboxWriter;

    /**
     * Выполняет мягкое удаление заявления.
     *
     * @param id идентификатор заявления
     */
    @Transactional
    public void delete(UUID id) {
        E entity = applicationAccessService.getActiveById(id, getApplicationType());
        entity.setDeleted(true);
        E saved = applicationRepository.save(entity);
        writeOutboxDeleted(saved);
    }

    /**
     * @return тип заявления
     */
    protected abstract ApplicationType getApplicationType();

    /**
     * Записывает outbox-события после удаления.
     *
     * @param entity сохранённое заявление
     */
    protected void writeOutboxDeleted(E entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        writeSubtypeOutboxDeleted(entity);
    }

    /**
     * Записывает outbox-события подтипа после удаления (при необходимости).
     *
     * @param entity сохранённое заявление
     */
    protected abstract void writeSubtypeOutboxDeleted(E entity);
}
