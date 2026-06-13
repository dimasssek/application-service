package ru.kubsu.applicationservice.mapper;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.dto.service.application.ProductApplicationUpdateRequest;

/**
 * Маппер продуктового заявления.
 */
@Component
public class ProductApplicationMapper {

    /** Общий маппер базовых полей заявления. */
    private final ApplicationBaseMapper applicationBaseMapper;

    /**
     * @param applicationBaseMapper общий маппер заявления
     */
    public ProductApplicationMapper(ApplicationBaseMapper applicationBaseMapper) {
        this.applicationBaseMapper = applicationBaseMapper;
    }

    /**
     * Заполняет специфичные поля заявления из запроса создания.
     *
     * @param request запрос создания
     * @param entity  заявление
     */
    public void fillFromCreate(ProductApplicationCreateRequest request, ProductApplication entity) {
        entity.setProductCategory(request.getProductCategory());
        entity.setProductCode(request.getProductCode());
        entity.setProductName(request.getProductName());
        entity.setAmount(request.getAmount());
        entity.setTermMonths(request.getTermMonths());
        entity.setPurpose(request.getPurpose());
        entity.setTariffCode(request.getTariffCode());
        entity.setExternalProductId(request.getExternalProductId());
    }

    /**
     * Обновляет специфичные поля заявления из запроса обновления.
     *
     * @param request запрос обновления
     * @param entity  заявление
     */
    public void updateFromRequest(ProductApplicationUpdateRequest request, ProductApplication entity) {
        entity.setProductCategory(request.getProductCategory());
        entity.setProductCode(request.getProductCode());
        entity.setProductName(request.getProductName());
        entity.setAmount(request.getAmount());
        entity.setTermMonths(request.getTermMonths());
        entity.setPurpose(request.getPurpose());
        entity.setTariffCode(request.getTariffCode());
        entity.setExternalProductId(request.getExternalProductId());
    }

    /**
     * Преобразует сущность в транспортный объект.
     *
     * @param entity заявление
     * @param reason причина статуса
     * @return DTO
     */
    public ProductApplicationTo toTo(ProductApplication entity, String reason) {
        ProductApplicationTo target = new ProductApplicationTo();
        applicationBaseMapper.fillTo(entity, target, reason);
        target.setProductCategory(entity.getProductCategory());
        target.setProductCode(entity.getProductCode());
        target.setProductName(entity.getProductName());
        target.setAmount(entity.getAmount());
        target.setTermMonths(entity.getTermMonths());
        target.setPurpose(entity.getPurpose());
        target.setTariffCode(entity.getTariffCode());
        target.setExternalProductId(entity.getExternalProductId());
        return target;
    }
}
