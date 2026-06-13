package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ApplicationTo;
import ru.kubsu.contracts.dto.service.application.ApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Общий маппер базовых полей заявления.
 */
@Component
public class ApplicationBaseMapper {

    /**
     * Заполняет базовые поля заявления из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    public void fillFromCreateRequest(ApplicationCreateRequest request, ApplicationEntity entity) {
        entity.setChannel(request.getChannel());
        entity.setLastName(request.getLastName());
        entity.setFirstName(request.getFirstName());
        entity.setPatronymic(request.getPatronymic());
        entity.setBirthDate(request.getBirthDate());
        entity.setGender(request.getGender());
        entity.setIdentityDocumentSeries(request.getIdentityDocumentSeries());
        entity.setIdentityDocumentNumber(request.getIdentityDocumentNumber());
        entity.setItn(request.getItn());
        entity.setInsuranceNumber(request.getInsuranceNumber());
    }

    /**
     * Обновляет базовые поля заявления из запроса обновления.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    public void updateFromRequest(ApplicationUpdateRequest request, ApplicationEntity entity) {
        entity.setLastName(request.getLastName());
        entity.setFirstName(request.getFirstName());
        entity.setPatronymic(request.getPatronymic());
        entity.setBirthDate(request.getBirthDate());
        entity.setGender(request.getGender());
        entity.setIdentityDocumentSeries(request.getIdentityDocumentSeries());
        entity.setIdentityDocumentNumber(request.getIdentityDocumentNumber());
        entity.setItn(request.getItn());
        entity.setInsuranceNumber(request.getInsuranceNumber());
    }

    /**
     * Заполняет базовые поля транспортного объекта заявления.
     *
     * @param entity заявление
     * @param target транспортный объект
     * @param reason причина статуса
     */
    public void fillTo(ApplicationEntity entity, ApplicationTo target, String reason) {
        target.setId(entity.getId());
        target.setClientId(entity.getClientId());
        target.setNumber(entity.getNumber());
        target.setApplicationType(resolveApplicationType(entity));
        target.setChannel(entity.getChannel());
        target.setStatusInternal(entity.getStatusInternal());
        target.setStatusBusiness(entity.getStatusBusiness());
        target.setReason(reason);
        target.setCreatedDate(entity.getCreatedDate());
        target.setClosedDate(entity.getClosedDate());
        target.setLastName(entity.getLastName());
        target.setFirstName(entity.getFirstName());
        target.setPatronymic(entity.getPatronymic());
        target.setBirthDate(entity.getBirthDate());
        target.setGender(entity.getGender());
        target.setIdentityDocumentSeries(entity.getIdentityDocumentSeries());
        target.setIdentityDocumentNumber(entity.getIdentityDocumentNumber());
        target.setItn(entity.getItn());
        target.setInsuranceNumber(entity.getInsuranceNumber());
    }

    private ApplicationType resolveApplicationType(ApplicationEntity entity) {
        if (entity.getApplicationType() != null) {
            return entity.getApplicationType();
        }
        return switch (entity) {
            case ProductApplication ignored -> ApplicationType.PRODUCT;
            case ServiceApplication ignored -> ApplicationType.SERVICE;
            case ProfileChangeApplication ignored -> ApplicationType.PROFILE_CHANGE;
            case DocumentApplication ignored -> ApplicationType.DOCUMENT;
            case ClaimApplication ignored -> ApplicationType.CLAIM;
            case AuthorityApplication ignored -> ApplicationType.AUTHORITY;
            default -> throw new IllegalStateException("Неизвестный тип заявления: " + entity.getClass());
        };
    }
}
