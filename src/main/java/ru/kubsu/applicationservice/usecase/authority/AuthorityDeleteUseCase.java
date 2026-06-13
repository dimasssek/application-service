package ru.kubsu.applicationservice.usecase.authority;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.usecase.base.BaseDeleteUseCase;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий мягкого удаления заявления на полномочия и доступы.
 */
@Service
public class AuthorityDeleteUseCase extends BaseDeleteUseCase<AuthorityApplication> {

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param applicationAccessService сервис доступа к заявлениям
     * @param outboxWriter             запись outbox-сообщений
     */
    public AuthorityDeleteUseCase(
            ApplicationRepository applicationRepository,
            ApplicationAccessService applicationAccessService,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationAccessService, outboxWriter);
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.AUTHORITY;
    }

    @Override
    protected void writeSubtypeOutboxDeleted(AuthorityApplication entity) {
        outboxWriter.writeAuthorityApplicationReport(entity, OutboxEventType.AUTHORITY_APPLICATION_UPDATED);
    }
}
