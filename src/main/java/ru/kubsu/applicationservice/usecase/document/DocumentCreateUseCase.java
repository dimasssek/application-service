package ru.kubsu.applicationservice.usecase.document;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.DocumentApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseCreateUseCase;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий создания заявления на получение документа.
 */
@Service
public class DocumentCreateUseCase extends BaseCreateUseCase<DocumentApplication,
        DocumentApplicationCreateRequest, DocumentApplicationTo> {

    /** Маппер заявления на получение документа. */
    private final DocumentApplicationMapper documentApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationNumberGenerator генератор номера заявления
     * @param applicationBaseMapper      общий маппер заявления
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param documentApplicationMapper  маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public DocumentCreateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationNumberGenerator applicationNumberGenerator,
            ApplicationBaseMapper applicationBaseMapper,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            DocumentApplicationMapper documentApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationNumberGenerator, applicationBaseMapper,
                statusInformService, preQueueCheckService);
        this.documentApplicationMapper = documentApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected DocumentApplication createEmptyEntity() {
        return new DocumentApplication();
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.DOCUMENT;
    }

    @Override
    protected void fillExtraFromCreateRequest(DocumentApplicationCreateRequest request, DocumentApplication entity) {
        documentApplicationMapper.fillFromCreate(request, entity);
    }

    @Override
    protected DocumentApplicationTo mapToTo(DocumentApplication entity, String reason) {
        return documentApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxCreated(DocumentApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeDocumentApplicationReport(entity, OutboxEventType.DOCUMENT_APPLICATION_CREATED);
    }
}
