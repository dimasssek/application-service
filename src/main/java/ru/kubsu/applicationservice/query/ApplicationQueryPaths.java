package ru.kubsu.applicationservice.query;

import ru.kubsu.applicationservice.entity.QApplicationEntity;

/**
 * Фабрика путей базовых полей заявления для QueryDSL-предикатов.
 */
public final class ApplicationQueryPaths {

    private ApplicationQueryPaths() {
    }

    /**
     * @param root Q-тип базового заявления
     * @return пути базовых полей
     */
    public static BaseApplicationPredicateBuilder.BasePaths from(QApplicationEntity root) {
        return new BaseApplicationPredicateBuilder.BasePaths(
                root.deleted,
                root.statusBusiness,
                root.number,
                root.createdDate,
                root.lastName,
                root.firstName,
                root.patronymic,
                root.identityDocumentSeries,
                root.identityDocumentNumber,
                root.itn,
                root.insuranceNumber,
                root.birthDate,
                root.gender);
    }
}
