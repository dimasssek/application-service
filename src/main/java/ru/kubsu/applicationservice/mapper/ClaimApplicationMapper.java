package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationTo;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationUpdateRequest;

/**
 * Маппер заявления-претензии или обращения.
 */
@Component
public class ClaimApplicationMapper {

    /** Общий маппер базовых полей заявления. */
    private final ApplicationBaseMapper applicationBaseMapper;

    /**
     * @param applicationBaseMapper общий маппер заявления
     */
    public ClaimApplicationMapper(ApplicationBaseMapper applicationBaseMapper) {
        this.applicationBaseMapper = applicationBaseMapper;
    }

    /**
     * Заполняет специфичные поля заявления из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    public void fillFromCreate(ClaimApplicationCreateRequest request, ClaimApplication entity) {
        entity.setClaimType(request.getClaimType());
        entity.setClaimSubject(request.getClaimSubject());
        entity.setDescription(request.getDescription());
        entity.setExternalRef(request.getExternalRef());
    }

    /**
     * Обновляет специфичные поля заявления из запроса обновления.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    public void updateFromRequest(ClaimApplicationUpdateRequest request, ClaimApplication entity) {
        entity.setClaimType(request.getClaimType());
        entity.setClaimSubject(request.getClaimSubject());
        entity.setDescription(request.getDescription());
        entity.setExternalRef(request.getExternalRef());
    }

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    public ClaimApplicationTo toTo(ClaimApplication entity, String reason) {
        ClaimApplicationTo target = new ClaimApplicationTo();
        applicationBaseMapper.fillTo(entity, target, reason);
        target.setClaimType(entity.getClaimType());
        target.setClaimSubject(entity.getClaimSubject());
        target.setDescription(entity.getDescription());
        target.setExternalRef(entity.getExternalRef());
        return target;
    }
}
