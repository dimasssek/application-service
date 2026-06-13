package ru.kubsu.applicationservice.usecase.product;

import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.mapper.ProductApplicationMapper;
import ru.kubsu.applicationservice.query.ProductApplicationPredicateBuilder;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchUseCase;
import ru.kubsu.contracts.dto.service.application.ProductApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий поиска продуктовых заявлений.
 */
@Service
public class ProductFetchUseCase extends BaseFetchUseCase<ProductApplication, ProductApplicationQueryParams, ProductApplicationTo> {

    /** Маппер продуктового заявления. */
    private final ProductApplicationMapper productApplicationMapper;

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param statusInformService      сервис status_inform
     * @param productApplicationMapper маппер подтипа
     */
    public ProductFetchUseCase(
            ApplicationRepository applicationRepository,
            StatusInformService statusInformService,
            ProductApplicationMapper productApplicationMapper) {
        super(applicationRepository, statusInformService);
        this.productApplicationMapper = productApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PRODUCT;
    }

    @Override
    protected Predicate buildPredicate(ProductApplicationQueryParams params) {
        return ProductApplicationPredicateBuilder.build(params);
    }

    @Override
    protected ProductApplicationTo mapToTo(ProductApplication entity, String reason) {
        return productApplicationMapper.toTo(entity, reason);
    }
}
