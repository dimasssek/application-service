package ru.kubsu.applicationservice.usecase.product;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ProductApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseCreateUseCase;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий создания продуктового заявления.
 */
@Service
public class ProductCreateUseCase extends BaseCreateUseCase<ProductApplication, ProductApplicationCreateRequest, ProductApplicationTo> {

    /** Маппер продуктового заявления. */
    private final ProductApplicationMapper productApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationNumberGenerator генератор номера заявления
     * @param applicationBaseMapper      общий маппер заявления
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param productApplicationMapper   маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public ProductCreateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationNumberGenerator applicationNumberGenerator,
            ApplicationBaseMapper applicationBaseMapper,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ProductApplicationMapper productApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationNumberGenerator, applicationBaseMapper,
                statusInformService, preQueueCheckService);
        this.productApplicationMapper = productApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ProductApplication createEmptyEntity() {
        return new ProductApplication();
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PRODUCT;
    }

    @Override
    protected void fillExtraFromCreateRequest(ProductApplicationCreateRequest request, ProductApplication entity) {
        productApplicationMapper.fillFromCreate(request, entity);
    }

    @Override
    protected ProductApplicationTo mapToTo(ProductApplication entity, String reason) {
        return productApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxCreated(ProductApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeProductApplicationReport(entity, OutboxEventType.PRODUCT_APPLICATION_CREATED);
    }
}
