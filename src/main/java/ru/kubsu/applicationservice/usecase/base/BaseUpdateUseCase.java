package ru.kubsu.applicationservice.usecase.base;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.dto.service.application.ApplicationTo;
import ru.kubsu.contracts.dto.service.application.ApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.UUID;

/**
 * Базовый сценарий обновления заявления.
 *
 * @param <E> тип сущности заявления
 * @param <U> тип запроса обновления
 * @param <T> тип транспортного объекта
 */
@RequiredArgsConstructor
public abstract class BaseUpdateUseCase<E extends ApplicationEntity,
        U extends ApplicationUpdateRequest, T extends ApplicationTo> {

    /** Репозиторий заявлений. */
    protected final ApplicationRepository applicationRepository;

    /** Маппер базовых полей заявления. */
    protected final ApplicationBaseMapper applicationBaseMapper;

    /** Сервис доступа к заявлениям. */
    protected final ApplicationAccessService applicationAccessService;

    /** Сервис status_inform. */
    protected final StatusInformService statusInformService;

    /** Сервис пре-очередных проверок. */
    protected final PreQueueCheckService preQueueCheckService;

    /**
     * Обновляет заявление.
     *
     * @param id      идентификатор заявления
     * @param request запрос обновления
     * @return обновлённое заявление
     */
    @Transactional
    public T update(UUID id, U request) {
        E entity = applicationAccessService.getActiveById(id, getApplicationType());
        applicationAccessService.ensureEditable(entity);

        applicationBaseMapper.updateFromRequest(request, entity);
        updateExtraFromRequest(request, entity);

        ApplicationStatusInternal statusInternal = preQueueCheckService.resolveStatus(
                entity, getApplicationType(), id);
        statusInformService.applyStatus(entity, getApplicationType(), statusInternal);

        E saved = applicationRepository.save(entity);
        writeOutboxUpdated(saved);
        return mapToTo(saved, statusInformService.resolveReason(saved, getApplicationType()));
    }

    /**
     * @return тип заявления
     */
    protected abstract ApplicationType getApplicationType();

    /**
     * Обновляет специфичные для подтипа поля.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    protected abstract void updateExtraFromRequest(U request, E entity);

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    protected abstract T mapToTo(E entity, String reason);

    /**
     * Записывает outbox-события после обновления.
     *
     * @param entity сохранённое заявление
     */
    protected abstract void writeOutboxUpdated(E entity);
}
