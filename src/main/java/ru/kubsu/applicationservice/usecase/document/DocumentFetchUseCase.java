package ru.kubsu.applicationservice.usecase.document;

import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.mapper.DocumentApplicationMapper;
import ru.kubsu.applicationservice.query.DocumentApplicationPredicateBuilder;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchUseCase;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий поиска заявлений на получение документов.
 */
@Service
public class DocumentFetchUseCase extends BaseFetchUseCase<DocumentApplication,
        DocumentApplicationQueryParams, DocumentApplicationTo> {

    /** Маппер заявления на получение документа. */
    private final DocumentApplicationMapper documentApplicationMapper;

    /**
     * @param applicationRepository     репозиторий заявлений
     * @param statusInformService       сервис status_inform
     * @param documentApplicationMapper маппер подтипа
     */
    public DocumentFetchUseCase(
            ApplicationRepository applicationRepository,
            StatusInformService statusInformService,
            DocumentApplicationMapper documentApplicationMapper) {
        super(applicationRepository, statusInformService);
        this.documentApplicationMapper = documentApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.DOCUMENT;
    }

    @Override
    protected Predicate buildPredicate(DocumentApplicationQueryParams params) {
        return DocumentApplicationPredicateBuilder.build(params);
    }

    @Override
    protected DocumentApplicationTo mapToTo(DocumentApplication entity, String reason) {
        return documentApplicationMapper.toTo(entity, reason);
    }
}
