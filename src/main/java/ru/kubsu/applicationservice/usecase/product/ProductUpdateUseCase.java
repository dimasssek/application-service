package ru.kubsu.applicationservice.usecase.product;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ProductApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseUpdateUseCase;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.dto.service.application.ProductApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий обновления продуктового заявления.
 */
@Service
public class ProductUpdateUseCase extends BaseUpdateUseCase<ProductApplication, ProductApplicationUpdateRequest, ProductApplicationTo> {

    /** Маппер продуктового заявления. */
    private final ProductApplicationMapper productApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationBaseMapper      общий маппер заявления
     * @param applicationAccessService   сервис доступа к заявлениям
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param productApplicationMapper   маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public ProductUpdateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationBaseMapper applicationBaseMapper,
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ProductApplicationMapper productApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationBaseMapper, applicationAccessService,
                statusInformService, preQueueCheckService);
        this.productApplicationMapper = productApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PRODUCT;
    }

    @Override
    protected void updateExtraFromRequest(ProductApplicationUpdateRequest request, ProductApplication entity) {
        productApplicationMapper.updateFromRequest(request, entity);
    }

    @Override
    protected ProductApplicationTo mapToTo(ProductApplication entity, String reason) {
        return productApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxUpdated(ProductApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeProductApplicationReport(entity, OutboxEventType.PRODUCT_APPLICATION_UPDATED);
    }
}
