package ru.kubsu.applicationservice.usecase.product;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.usecase.base.BaseDeleteUseCase;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий мягкого удаления продуктового заявления.
 */
@Service
public class ProductDeleteUseCase extends BaseDeleteUseCase<ProductApplication> {

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param applicationAccessService сервис доступа к заявлениям
     * @param outboxWriter             запись outbox-сообщений
     */
    public ProductDeleteUseCase(
            ApplicationRepository applicationRepository,
            ApplicationAccessService applicationAccessService,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationAccessService, outboxWriter);
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PRODUCT;
    }

    @Override
    protected void writeSubtypeOutboxDeleted(ProductApplication entity) {
        outboxWriter.writeProductApplicationReport(entity, OutboxEventType.PRODUCT_APPLICATION_UPDATED);
    }
}
