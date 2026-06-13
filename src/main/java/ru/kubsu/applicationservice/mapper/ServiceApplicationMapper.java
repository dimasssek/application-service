package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationTo;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationUpdateRequest;

/**
 * Маппер заявления на банковскую услугу.
 */
@Component
public class ServiceApplicationMapper {

    /** Общий маппер базовых полей заявления. */
    private final ApplicationBaseMapper applicationBaseMapper;

    /**
     * @param applicationBaseMapper общий маппер заявления
     */
    public ServiceApplicationMapper(ApplicationBaseMapper applicationBaseMapper) {
        this.applicationBaseMapper = applicationBaseMapper;
    }

    /**
     * Заполняет специфичные поля заявления из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    public void fillFromCreate(ServiceApplicationCreateRequest request, ServiceApplication entity) {
        entity.setServiceType(request.getServiceType());
        entity.setServiceName(request.getServiceName());
        entity.setActionType(request.getActionType());
    }

    /**
     * Обновляет специфичные поля заявления из запроса обновления.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    public void updateFromRequest(ServiceApplicationUpdateRequest request, ServiceApplication entity) {
        entity.setServiceType(request.getServiceType());
        entity.setServiceName(request.getServiceName());
        entity.setActionType(request.getActionType());
    }

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    public ServiceApplicationTo toTo(ServiceApplication entity, String reason) {
        ServiceApplicationTo target = new ServiceApplicationTo();
        applicationBaseMapper.fillTo(entity, target, reason);
        target.setServiceType(entity.getServiceType());
        target.setServiceName(entity.getServiceName());
        target.setActionType(entity.getActionType());
        return target;
    }
}
