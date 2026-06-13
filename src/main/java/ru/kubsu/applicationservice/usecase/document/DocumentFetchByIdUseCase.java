package ru.kubsu.applicationservice.usecase.document;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.mapper.DocumentApplicationMapper;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchByIdUseCase;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий получения заявления на получение документа по идентификатору.
 */
@Service
public class DocumentFetchByIdUseCase extends BaseFetchByIdUseCase<DocumentApplication, DocumentApplicationTo> {

    /** Маппер заявления на получение документа. */
    private final DocumentApplicationMapper documentApplicationMapper;

    /**
     * @param applicationAccessService  сервис доступа к заявлениям
     * @param statusInformService       сервис status_inform
     * @param documentApplicationMapper маппер подтипа
     */
    public DocumentFetchByIdUseCase(
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            DocumentApplicationMapper documentApplicationMapper) {
        super(applicationAccessService, statusInformService);
        this.documentApplicationMapper = documentApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.DOCUMENT;
    }

    @Override
    protected DocumentApplicationTo mapToTo(DocumentApplication entity, String reason) {
        return documentApplicationMapper.toTo(entity, reason);
    }
}
