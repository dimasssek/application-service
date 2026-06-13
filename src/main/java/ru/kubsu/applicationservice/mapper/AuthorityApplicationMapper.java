package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationTo;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationUpdateRequest;

/**
 * Маппер заявления на полномочия и доступы.
 */
@Component
public class AuthorityApplicationMapper {

    /** Общий маппер базовых полей заявления. */
    private final ApplicationBaseMapper applicationBaseMapper;

    /**
     * @param applicationBaseMapper общий маппер заявления
     */
    public AuthorityApplicationMapper(ApplicationBaseMapper applicationBaseMapper) {
        this.applicationBaseMapper = applicationBaseMapper;
    }

    /**
     * Заполняет специфичные поля заявления из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    public void fillFromCreate(AuthorityApplicationCreateRequest request, AuthorityApplication entity) {
        entity.setAuthorityType(request.getAuthorityType());
        entity.setPersonName(request.getPersonName());
        entity.setDocumentNumber(request.getDocumentNumber());
        entity.setValidUntil(request.getValidUntil());
    }

    /**
     * Обновляет специфичные поля заявления из запроса обновления.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    public void updateFromRequest(AuthorityApplicationUpdateRequest request, AuthorityApplication entity) {
        entity.setAuthorityType(request.getAuthorityType());
        entity.setPersonName(request.getPersonName());
        entity.setDocumentNumber(request.getDocumentNumber());
        entity.setValidUntil(request.getValidUntil());
    }

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    public AuthorityApplicationTo toTo(AuthorityApplication entity, String reason) {
        AuthorityApplicationTo target = new AuthorityApplicationTo();
        applicationBaseMapper.fillTo(entity, target, reason);
        target.setAuthorityType(entity.getAuthorityType());
        target.setPersonName(entity.getPersonName());
        target.setDocumentNumber(entity.getDocumentNumber());
        target.setValidUntil(entity.getValidUntil());
        return target;
    }
}
