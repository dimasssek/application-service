package ru.kubsu.applicationservice.usecase.process;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.DocumentApplicationRepository;
import ru.kubsu.applicationservice.service.ClientDataVerificationService;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Post-processing заявления на документ в general-check.
 */
@Service
public class DocumentProcessUseCase extends BaseProcessUseCase<DocumentApplication> {

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param typedRepository     репозиторий заявлений на документы
     * @param clientLookupService сервис поиска клиента
     * @param statusInformService сервис status_inform
     * @param outboxWriter        запись outbox-сообщений
     */
    public DocumentProcessUseCase(DocumentApplicationRepository typedRepository,
                                  ClientLookupService clientLookupService,
                                  ClientDataVerificationService clientDataVerificationService,
                                  StatusInformService statusInformService,
                                  OutboxWriter outboxWriter) {
        super(typedRepository, clientLookupService, clientDataVerificationService, statusInformService);
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.DOCUMENT;
    }

    @Override
    protected void writeOutboxUpdated(DocumentApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeDocumentApplicationReport(entity, OutboxEventType.DOCUMENT_APPLICATION_UPDATED);
    }
}
