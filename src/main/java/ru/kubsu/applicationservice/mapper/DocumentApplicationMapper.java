package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationTo;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationUpdateRequest;

/**
 * Маппер заявления на получение документа.
 */
@Component
public class DocumentApplicationMapper {

    /** Общий маппер базовых полей заявления. */
    private final ApplicationBaseMapper applicationBaseMapper;

    /**
     * @param applicationBaseMapper общий маппер заявления
     */
    public DocumentApplicationMapper(ApplicationBaseMapper applicationBaseMapper) {
        this.applicationBaseMapper = applicationBaseMapper;
    }

    /**
     * Заполняет специфичные поля заявления из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    public void fillFromCreate(DocumentApplicationCreateRequest request, DocumentApplication entity) {
        entity.setDocumentType(request.getDocumentType());
        entity.setDeliveryType(request.getDeliveryType());
        entity.setPurpose(request.getPurpose());
    }

    /**
     * Обновляет специфичные поля заявления из запроса обновления.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    public void updateFromRequest(DocumentApplicationUpdateRequest request, DocumentApplication entity) {
        entity.setDocumentType(request.getDocumentType());
        entity.setDeliveryType(request.getDeliveryType());
        entity.setPurpose(request.getPurpose());
    }

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    public DocumentApplicationTo toTo(DocumentApplication entity, String reason) {
        DocumentApplicationTo target = new DocumentApplicationTo();
        applicationBaseMapper.fillTo(entity, target, reason);
        target.setDocumentType(entity.getDocumentType());
        target.setDeliveryType(entity.getDeliveryType());
        target.setPurpose(entity.getPurpose());
        return target;
    }
}
