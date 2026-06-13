package ru.kubsu.applicationservice.usecase.claim;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.usecase.base.BaseDeleteUseCase;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий мягкого удаления заявления-претензии или обращения.
 */
@Service
public class ClaimDeleteUseCase extends BaseDeleteUseCase<ClaimApplication> {

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param applicationAccessService сервис доступа к заявлениям
     * @param outboxWriter             запись outbox-сообщений
     */
    public ClaimDeleteUseCase(
            ApplicationRepository applicationRepository,
            ApplicationAccessService applicationAccessService,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationAccessService, outboxWriter);
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.CLAIM;
    }

    @Override
    protected void writeSubtypeOutboxDeleted(ClaimApplication entity) {
        outboxWriter.writeClaimApplicationReport(entity, OutboxEventType.CLAIM_APPLICATION_UPDATED);
    }
}
