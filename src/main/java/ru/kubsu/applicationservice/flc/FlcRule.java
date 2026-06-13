package ru.kubsu.applicationservice.flc;

import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.Optional;

/**
 * Правило форматно-логического контроля заявления.
 */
public interface FlcRule {

    /**
     * Проверяет заявление и возвращает статус ошибки ФЛК, если правило нарушено.
     *
     * @param entity          заявление
     * @param applicationType тип заявления
     * @return статус ошибки ФЛК или пусто, если правило пройдено
     */
    Optional<ApplicationStatusInternal> check(ApplicationEntity entity, ApplicationType applicationType);
}
