package ru.kubsu.applicationservice.upload;

import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Схема полей файла загрузки заявлений.
 */
public final class ApplicationUploadFieldSchema {

    /** Количество базовых полей клиента в каждой строке данных. */
    public static final int BASE_FIELD_COUNT = 9;

    private ApplicationUploadFieldSchema() {
    }

    /**
     * Возвращает общее количество полей в строке данных для типа заявления.
     *
     * @param applicationType тип заявления
     * @return количество полей
     */
    public static int totalFieldCount(ApplicationType applicationType) {
        return BASE_FIELD_COUNT + typeSpecificFieldCount(applicationType);
    }

    private static int typeSpecificFieldCount(ApplicationType applicationType) {
        return switch (applicationType) {
            case PRODUCT -> 8;
            case SERVICE -> 3;
            case PROFILE_CHANGE -> 3;
            case DOCUMENT -> 3;
            case CLAIM -> 4;
            case AUTHORITY -> 4;
        };
    }
}
