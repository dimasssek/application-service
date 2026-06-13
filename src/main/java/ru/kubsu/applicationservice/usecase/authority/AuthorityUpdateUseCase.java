package ru.kubsu.applicationservice.usecase.authority;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.AuthorityApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseUpdateUseCase;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationTo;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий обновления заявления на полномочия и доступы.
 */
@Service
public class AuthorityUpdateUseCase extends BaseUpdateUseCase<AuthorityApplication,
        AuthorityApplicationUpdateRequest, AuthorityApplicationTo> {

    /** Маппер заявления на полномочия и доступы. */
    private final AuthorityApplicationMapper authorityApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationBaseMapper      общий маппер заявления
     * @param applicationAccessService   сервис доступа к заявлениям
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param authorityApplicationMapper маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public AuthorityUpdateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationBaseMapper applicationBaseMapper,
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            AuthorityApplicationMapper authorityApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationBaseMapper, applicationAccessService,
                statusInformService, preQueueCheckService);
        this.authorityApplicationMapper = authorityApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.AUTHORITY;
    }

    @Override
    protected void updateExtraFromRequest(AuthorityApplicationUpdateRequest request, AuthorityApplication entity) {
        authorityApplicationMapper.updateFromRequest(request, entity);
    }

    @Override
    protected AuthorityApplicationTo mapToTo(AuthorityApplication entity, String reason) {
        return authorityApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxUpdated(AuthorityApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeAuthorityApplicationReport(entity, OutboxEventType.AUTHORITY_APPLICATION_UPDATED);
    }
}
