package ru.kubsu.applicationservice.usecase.process;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ServiceApplicationRepository;
import ru.kubsu.applicationservice.service.ClientDataVerificationService;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Post-processing заявления на банковскую услугу в general-check.
 */
@Service
public class ServiceProcessUseCase extends BaseProcessUseCase<ServiceApplication> {

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param typedRepository     репозиторий заявлений на услуги
     * @param clientLookupService сервис поиска клиента
     * @param statusInformService сервис status_inform
     * @param outboxWriter        запись outbox-сообщений
     */
    public ServiceProcessUseCase(ServiceApplicationRepository typedRepository,
                                 ClientLookupService clientLookupService,
                                 ClientDataVerificationService clientDataVerificationService,
                                 StatusInformService statusInformService,
                                 OutboxWriter outboxWriter) {
        super(typedRepository, clientLookupService, clientDataVerificationService, statusInformService);
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.SERVICE;
    }

    @Override
    protected void writeOutboxUpdated(ServiceApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeServiceApplicationReport(entity, OutboxEventType.SERVICE_APPLICATION_UPDATED);
    }
}
