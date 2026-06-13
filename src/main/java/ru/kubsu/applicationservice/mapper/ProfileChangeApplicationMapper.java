package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationTo;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationUpdateRequest;

/**
 * Маппер заявления на изменение данных клиента.
 */
@Component
public class ProfileChangeApplicationMapper {

    /** Общий маппер базовых полей заявления. */
    private final ApplicationBaseMapper applicationBaseMapper;

    /**
     * @param applicationBaseMapper общий маппер заявления
     */
    public ProfileChangeApplicationMapper(ApplicationBaseMapper applicationBaseMapper) {
        this.applicationBaseMapper = applicationBaseMapper;
    }

    /**
     * Заполняет специфичные поля заявления из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    public void fillFromCreate(ProfileChangeApplicationCreateRequest request, ProfileChangeApplication entity) {
        entity.setChangeType(request.getChangeType());
        entity.setOldValue(request.getOldValue());
        entity.setNewValue(request.getNewValue());
    }

    /**
     * Обновляет специфичные поля заявления из запроса обновления.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    public void updateFromRequest(ProfileChangeApplicationUpdateRequest request, ProfileChangeApplication entity) {
        entity.setChangeType(request.getChangeType());
        entity.setOldValue(request.getOldValue());
        entity.setNewValue(request.getNewValue());
    }

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    public ProfileChangeApplicationTo toTo(ProfileChangeApplication entity, String reason) {
        ProfileChangeApplicationTo target = new ProfileChangeApplicationTo();
        applicationBaseMapper.fillTo(entity, target, reason);
        target.setChangeType(entity.getChangeType());
        target.setOldValue(entity.getOldValue());
        target.setNewValue(entity.getNewValue());
        return target;
    }
}
