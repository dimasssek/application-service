package ru.kubsu.applicationservice.usecase.document;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.DocumentApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseUpdateUseCase;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationTo;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий обновления заявления на получение документа.
 */
@Service
public class DocumentUpdateUseCase extends BaseUpdateUseCase<DocumentApplication,
        DocumentApplicationUpdateRequest, DocumentApplicationTo> {

    /** Маппер заявления на получение документа. */
    private final DocumentApplicationMapper documentApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository     репозиторий заявлений
     * @param applicationBaseMapper     общий маппер заявления
     * @param applicationAccessService  сервис доступа к заявлениям
     * @param statusInformService       сервис status_inform
     * @param preQueueCheckService      сервис пре-очередных проверок
     * @param documentApplicationMapper маппер подтипа
     * @param outboxWriter              запись outbox-сообщений
     */
    public DocumentUpdateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationBaseMapper applicationBaseMapper,
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            DocumentApplicationMapper documentApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationBaseMapper, applicationAccessService,
                statusInformService, preQueueCheckService);
        this.documentApplicationMapper = documentApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.DOCUMENT;
    }

    @Override
    protected void updateExtraFromRequest(DocumentApplicationUpdateRequest request, DocumentApplication entity) {
        documentApplicationMapper.updateFromRequest(request, entity);
    }

    @Override
    protected DocumentApplicationTo mapToTo(DocumentApplication entity, String reason) {
        return documentApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxUpdated(DocumentApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeDocumentApplicationReport(entity, OutboxEventType.DOCUMENT_APPLICATION_UPDATED);
    }
}
