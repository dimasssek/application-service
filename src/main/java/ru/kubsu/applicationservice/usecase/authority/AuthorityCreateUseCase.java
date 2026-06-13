package ru.kubsu.applicationservice.usecase.authority;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.AuthorityApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseCreateUseCase;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий создания заявления на полномочия и доступы.
 */
@Service
public class AuthorityCreateUseCase extends BaseCreateUseCase<AuthorityApplication,
        AuthorityApplicationCreateRequest, AuthorityApplicationTo> {

    /** Маппер заявления на полномочия и доступы. */
    private final AuthorityApplicationMapper authorityApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationNumberGenerator генератор номера заявления
     * @param applicationBaseMapper      общий маппер заявления
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param authorityApplicationMapper маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public AuthorityCreateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationNumberGenerator applicationNumberGenerator,
            ApplicationBaseMapper applicationBaseMapper,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            AuthorityApplicationMapper authorityApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationNumberGenerator, applicationBaseMapper,
                statusInformService, preQueueCheckService);
        this.authorityApplicationMapper = authorityApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected AuthorityApplication createEmptyEntity() {
        return new AuthorityApplication();
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.AUTHORITY;
    }

    @Override
    protected void fillExtraFromCreateRequest(AuthorityApplicationCreateRequest request,
                                              AuthorityApplication entity) {
        authorityApplicationMapper.fillFromCreate(request, entity);
    }

    @Override
    protected AuthorityApplicationTo mapToTo(AuthorityApplication entity, String reason) {
        return authorityApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxCreated(AuthorityApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeAuthorityApplicationReport(entity, OutboxEventType.AUTHORITY_APPLICATION_CREATED);
    }
}
