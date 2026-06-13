package ru.kubsu.applicationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.StatusInform;
import ru.kubsu.applicationservice.repository.StatusInformRepository;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.exception.service.application.ApplicationValidationException;

/**
 * Сервис установки статусов заявления по матрице status_inform.
 */
@Service
@RequiredArgsConstructor
public class StatusInformService {

    /** Репозиторий матрицы статусов. */
    private final StatusInformRepository statusInformRepository;

    /**
     * Выставляет внутренний и бизнес-статус заявления по правилу status_inform.
     *
     * @param entity           заявление
     * @param applicationType  тип заявления
     * @param statusInternal   внутренний статус
     */
    public void applyStatus(ApplicationEntity entity,
                            ApplicationType applicationType,
                            ApplicationStatusInternal statusInternal) {
        StatusInform statusInform = statusInformRepository.findByStatusInternal(statusInternal)
                .orElseThrow(() -> new ApplicationValidationException(
                        "Не найдено правило status_inform для статуса: " + statusInternal));

        entity.setStatusInternal(statusInternal);
        entity.setStatusBusiness(resolveBusinessStatus(statusInform, applicationType));
    }

    /**
     * Возвращает текст причины статуса для UI.
     *
     * @param entity          заявление
     * @param applicationType тип заявления
     * @return причина из status_inform
     */
    public String resolveReason(ApplicationEntity entity, ApplicationType applicationType) {
        return statusInformRepository.findByStatusInternal(entity.getStatusInternal())
                .map(StatusInform::getReason)
                .orElse(null);
    }

    private ApplicationStatusBusiness resolveBusinessStatus(StatusInform statusInform,
                                                            ApplicationType applicationType) {
        return switch (applicationType) {
            case PRODUCT -> statusInform.getProductStatusBusiness();
            case SERVICE -> statusInform.getServiceStatusBusiness();
            case PROFILE_CHANGE -> statusInform.getProfileChangeStatusBusiness();
            case DOCUMENT -> statusInform.getDocumentStatusBusiness();
            case CLAIM -> statusInform.getClaimStatusBusiness();
            case AUTHORITY -> statusInform.getAuthorityStatusBusiness();
        };
    }
}
