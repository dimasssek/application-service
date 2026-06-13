package ru.kubsu.applicationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.exception.service.application.ApplicationNotFoundException;
import ru.kubsu.contracts.exception.service.application.ApplicationValidationException;

import java.util.UUID;

/**
 * Сервис загрузки заявлений с проверкой типа и признака удаления.
 */
@Service
@RequiredArgsConstructor
public class ApplicationAccessService {

    private static final String NOT_EDITABLE_MESSAGE =
            "Заявление в конечном статусе нельзя изменить";

    /** Репозиторий заявлений. */
    private final ApplicationRepository applicationRepository;

    /**
     * Загружает активное заявление нужного типа.
     *
     * @param id              идентификатор
     * @param applicationType ожидаемый тип
     * @param <E>             тип заявления
     * @return заявление
     */
    @SuppressWarnings("unchecked")
    public <E extends ApplicationEntity> E getActiveById(UUID id, ApplicationType applicationType) {
        ApplicationEntity entity = applicationRepository.findById(id)
                .filter(application -> !application.isDeleted())
                .filter(application -> matchesType(application, applicationType))
                .orElseThrow(() -> new ApplicationNotFoundException(id));
        return (E) entity;
    }

    /**
     * Проверяет, что заявление можно редактировать (статусы IN_QUEUE или BAD).
     *
     * @param entity заявление
     */
    public void ensureEditable(ApplicationEntity entity) {
        ApplicationStatusBusiness status = entity.getStatusBusiness();
        if (status != ApplicationStatusBusiness.IN_QUEUE && status != ApplicationStatusBusiness.BAD) {
            throw new ApplicationValidationException(NOT_EDITABLE_MESSAGE);
        }
    }

    private boolean matchesType(ApplicationEntity entity, ApplicationType applicationType) {
        return switch (applicationType) {
            case PRODUCT -> entity instanceof ProductApplication;
            case SERVICE -> entity instanceof ServiceApplication;
            case PROFILE_CHANGE -> entity instanceof ProfileChangeApplication;
            case DOCUMENT -> entity instanceof DocumentApplication;
            case CLAIM -> entity instanceof ClaimApplication;
            case AUTHORITY -> entity instanceof AuthorityApplication;
        };
    }
}
