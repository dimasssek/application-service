package ru.kubsu.applicationservice.usecase.base;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.time.OffsetDateTime;

/**
 * Базовый сценарий создания заявления с пре-очередными проверками.
 *
 * @param <E> тип сущности заявления
 * @param <C> тип запроса создания
 * @param <T> тип транспортного объекта
 */
@RequiredArgsConstructor
public abstract class BaseCreateUseCase<E extends ApplicationEntity, C extends ApplicationCreateRequest, T extends ApplicationTo> {

    /** Репозиторий заявлений. */
    protected final ApplicationRepository applicationRepository;

    /** Генератор номера заявления. */
    protected final ApplicationNumberGenerator applicationNumberGenerator;

    /** Маппер базовых полей заявления. */
    protected final ApplicationBaseMapper applicationBaseMapper;

    /** Сервис status_inform. */
    protected final StatusInformService statusInformService;

    /** Сервис пре-очередных проверок. */
    protected final PreQueueCheckService preQueueCheckService;

    /**
     * Создаёт заявление.
     *
     * @param request запрос создания
     * @return созданное заявление
     */
    @Transactional
    public T create(C request) {
        E entity = createEmptyEntity();
        entity.setDeleted(false);
        entity.setNumber(applicationNumberGenerator.nextNumber(getApplicationType()));
        entity.setCreatedDate(OffsetDateTime.now());
        applicationBaseMapper.fillFromCreateRequest(request, entity);
        fillExtraFromCreateRequest(request, entity);

        ApplicationStatusInternal statusInternal = preQueueCheckService.resolveStatus(
                entity, getApplicationType(), null);
        statusInformService.applyStatus(entity, getApplicationType(), statusInternal);

        E saved = applicationRepository.save(entity);
        writeOutboxCreated(saved);
        return mapToTo(saved, statusInformService.resolveReason(saved, getApplicationType()));
    }

    /**
     * @return пустая сущность подтипа
     */
    protected abstract E createEmptyEntity();

    /**
     * @return тип заявления
     */
    protected abstract ApplicationType getApplicationType();

    /**
     * Заполняет специфичные для подтипа поля из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    protected abstract void fillExtraFromCreateRequest(C request, E entity);

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    protected abstract T mapToTo(E entity, String reason);

    /**
     * Записывает outbox-события после создания.
     *
     * @param entity сохранённое заявление
     */
    protected abstract void writeOutboxCreated(E entity);
}
