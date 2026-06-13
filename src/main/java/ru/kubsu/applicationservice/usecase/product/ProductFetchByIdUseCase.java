package ru.kubsu.applicationservice.usecase.product;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.mapper.ProductApplicationMapper;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchByIdUseCase;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий получения продуктового заявления по идентификатору.
 */
@Service
public class ProductFetchByIdUseCase extends BaseFetchByIdUseCase<ProductApplication, ProductApplicationTo> {

    /** Маппер продуктового заявления. */
    private final ProductApplicationMapper productApplicationMapper;

    /**
     * @param applicationAccessService сервис доступа к заявлениям
     * @param statusInformService      сервис status_inform
     * @param productApplicationMapper маппер подтипа
     */
    public ProductFetchByIdUseCase(
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            ProductApplicationMapper productApplicationMapper) {
        super(applicationAccessService, statusInformService);
        this.productApplicationMapper = productApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PRODUCT;
    }

    @Override
    protected ProductApplicationTo mapToTo(ProductApplication entity, String reason) {
        return productApplicationMapper.toTo(entity, reason);
    }
}
