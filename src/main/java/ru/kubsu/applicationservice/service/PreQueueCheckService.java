package ru.kubsu.applicationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.flc.FlcCheckService;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.UUID;

/**
 * Пре-очередные проверки заявления: ФЛК и дубликаты.
 */
@Service
@RequiredArgsConstructor
public class PreQueueCheckService {

    /** Сервис ФЛК. */
    private final FlcCheckService flcCheckService;

    /** Сервис проверки дубликатов. */
    private final DuplicateCheckService duplicateCheckService;

    /**
     * Определяет внутренний статус заявления после пре-очередных проверок.
     *
     * @param entity          заявление
     * @param applicationType тип заявления
     * @param excludeId       идентификатор, исключаемый из проверки дубликатов
     * @return внутренний статус
     */
    public ApplicationStatusInternal resolveStatus(ApplicationEntity entity,
                                                   ApplicationType applicationType,
                                                   UUID excludeId) {
        return flcCheckService.check(entity, applicationType)
                .orElseGet(() -> duplicateCheckService.isDuplicate(entity, applicationType, excludeId)
                        ? ApplicationStatusInternal.DUPLICATE
                        : ApplicationStatusInternal.IN_QUEUE);
    }
}
