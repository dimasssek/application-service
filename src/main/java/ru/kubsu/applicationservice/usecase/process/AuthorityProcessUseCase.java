package ru.kubsu.applicationservice.usecase.process;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.AuthorityApplicationRepository;
import ru.kubsu.applicationservice.service.ClientDataVerificationService;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Post-processing заявления на полномочия в general-check.
 */
@Service
public class AuthorityProcessUseCase extends BaseProcessUseCase<AuthorityApplication> {

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param typedRepository     репозиторий заявлений на полномочия
     * @param clientLookupService сервис поиска клиента
     * @param statusInformService сервис status_inform
     * @param outboxWriter        запись outbox-сообщений
     */
    public AuthorityProcessUseCase(AuthorityApplicationRepository typedRepository,
                                   ClientLookupService clientLookupService,
                                   ClientDataVerificationService clientDataVerificationService,
                                   StatusInformService statusInformService,
                                   OutboxWriter outboxWriter) {
        super(typedRepository, clientLookupService, clientDataVerificationService, statusInformService);
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.AUTHORITY;
    }

    @Override
    protected void writeOutboxUpdated(AuthorityApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeAuthorityApplicationReport(entity, OutboxEventType.AUTHORITY_APPLICATION_UPDATED);
    }
}
