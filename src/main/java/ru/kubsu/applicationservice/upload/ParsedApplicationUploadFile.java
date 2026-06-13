package ru.kubsu.applicationservice.upload;

import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.List;

/**
 * Результат разбора файла загрузки заявлений.
 *
 * @param applicationType тип заявления из первой строки файла
 * @param requests        запросы на создание заявлений
 */
public record ParsedApplicationUploadFile(ApplicationType applicationType,
                                          List<ApplicationCreateRequest> requests) {
}
