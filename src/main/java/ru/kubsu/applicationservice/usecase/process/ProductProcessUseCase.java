package ru.kubsu.applicationservice.usecase.process;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ProductApplicationRepository;
import ru.kubsu.applicationservice.service.ClientDataVerificationService;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Post-processing продуктового заявления в general-check.
 */
@Service
public class ProductProcessUseCase extends BaseProcessUseCase<ProductApplication> {

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param typedRepository     репозиторий продуктовых заявлений
     * @param clientLookupService сервис поиска клиента
     * @param clientDataVerificationService сервис сверки ИНН и СНИЛС
     * @param statusInformService           сервис status_inform
     * @param outboxWriter                  запись outbox-сообщений
     */
    public ProductProcessUseCase(ProductApplicationRepository typedRepository,
                                 ClientLookupService clientLookupService,
                                 ClientDataVerificationService clientDataVerificationService,
                                 StatusInformService statusInformService,
                                 OutboxWriter outboxWriter) {
        super(typedRepository, clientLookupService, clientDataVerificationService, statusInformService);
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PRODUCT;
    }

    @Override
    protected void writeOutboxUpdated(ProductApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeProductApplicationReport(entity, OutboxEventType.PRODUCT_APPLICATION_UPDATED);
    }
}
