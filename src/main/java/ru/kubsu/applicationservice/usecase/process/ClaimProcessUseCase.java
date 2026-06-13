package ru.kubsu.applicationservice.usecase.process;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ClaimApplicationRepository;
import ru.kubsu.applicationservice.service.ClientDataVerificationService;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Post-processing заявления на претензию в general-check.
 */
@Service
public class ClaimProcessUseCase extends BaseProcessUseCase<ClaimApplication> {

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param typedRepository     репозиторий заявлений на претензии
     * @param clientLookupService сервис поиска клиента
     * @param statusInformService сервис status_inform
     * @param outboxWriter        запись outbox-сообщений
     */
    public ClaimProcessUseCase(ClaimApplicationRepository typedRepository,
                               ClientLookupService clientLookupService,
                               ClientDataVerificationService clientDataVerificationService,
                               StatusInformService statusInformService,
                               OutboxWriter outboxWriter) {
        super(typedRepository, clientLookupService, clientDataVerificationService, statusInformService);
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.CLAIM;
    }

    @Override
    protected void writeOutboxUpdated(ClaimApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeClaimApplicationReport(entity, OutboxEventType.CLAIM_APPLICATION_UPDATED);
    }
}
