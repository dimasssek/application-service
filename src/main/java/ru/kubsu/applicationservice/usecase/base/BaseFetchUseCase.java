package ru.kubsu.applicationservice.usecase.base;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.query.ApplicationPageableFactory;
import ru.kubsu.applicationservice.query.ApplicationSortParser;
import ru.kubsu.applicationservice.query.PageDataBuilder;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.dto.common.PageData;
import ru.kubsu.contracts.dto.service.application.ApplicationTo;
import ru.kubsu.contracts.dto.service.application.BaseApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Базовый сценарий поиска заявлений.
 *
 * @param <E> тип сущности заявления
 * @param <Q> тип параметров поиска
 * @param <T> тип транспортного объекта
 */
@RequiredArgsConstructor
public abstract class BaseFetchUseCase<E extends ApplicationEntity,
        Q extends BaseApplicationQueryParams, T extends ApplicationTo> {

    /** Репозиторий заявлений. */
    protected final ApplicationRepository applicationRepository;

    /** Сервис status_inform. */
    protected final StatusInformService statusInformService;

    /**
     * Выполняет поиск заявлений.
     *
     * @param params параметры поиска
     * @return страница DTO
     */
    @Transactional(readOnly = true)
    public PageData<T> fetch(Q params) {
        Predicate predicate = buildPredicate(params);
        OrderSpecifier<?> orderSpecifier = ApplicationSortParser.parse(params.getSortKey());
        Pageable pageable = ApplicationPageableFactory.from(params, orderSpecifier);

        Page<E> page = applicationRepository.findAll(predicate, pageable)
                .map(entity -> castEntity(entity));

        return PageDataBuilder.from(page, entity -> mapToTo(entity,
                statusInformService.resolveReason(entity, getApplicationType())));
    }

    /**
     * @return тип заявления
     */
    protected abstract ApplicationType getApplicationType();

    /**
     * Формирует QueryDSL-предикат по параметрам поиска.
     *
     * @param params параметры поиска
     * @return предикат
     */
    protected abstract Predicate buildPredicate(Q params);

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    protected abstract T mapToTo(E entity, String reason);

    /**
     * Приводит полиморфную сущность к ожидаемому подтипу.
     *
     * @param entity заявление
     * @return сущность подтипа
     */
    @SuppressWarnings("unchecked")
    protected E castEntity(ApplicationEntity entity) {
        return (E) entity;
    }
}
