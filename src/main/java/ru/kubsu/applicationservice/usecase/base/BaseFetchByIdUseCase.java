package ru.kubsu.applicationservice.usecase.base;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.dto.service.application.ApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.UUID;

/**
 * Базовый сценарий получения заявления по идентификатору.
 *
 * @param <E> тип сущности заявления
 * @param <T> тип транспортного объекта
 */
@RequiredArgsConstructor
public abstract class BaseFetchByIdUseCase<E extends ApplicationEntity, T extends ApplicationTo> {

    /** Сервис доступа к заявлениям. */
    protected final ApplicationAccessService applicationAccessService;

    /** Сервис status_inform. */
    protected final StatusInformService statusInformService;

    /**
     * Возвращает заявление по идентификатору.
     *
     * @param id идентификатор заявления
     * @return DTO заявления
     */
    @Transactional(readOnly = true)
    public T fetchById(UUID id) {
        E entity = applicationAccessService.getActiveById(id, getApplicationType());
        return mapToTo(entity, statusInformService.resolveReason(entity, getApplicationType()));
    }

    /**
     * @return тип заявления
     */
    protected abstract ApplicationType getApplicationType();

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    protected abstract T mapToTo(E entity, String reason);
}
