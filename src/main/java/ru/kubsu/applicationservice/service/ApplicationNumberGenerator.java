package ru.kubsu.applicationservice.service;

import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Генератор номера заявления по типу.
 */
public interface ApplicationNumberGenerator {

    /**
     * Возвращает следующий номер заявления для указанного типа.
     *
     * @param applicationType тип заявления
     * @return номер в формате BSPS-{code}-{n}
     */
    String nextNumber(ApplicationType applicationType);
}
