package ru.kubsu.applicationservice.usecase.claim;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ClaimApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseUpdateUseCase;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationTo;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий обновления заявления-претензии или обращения.
 */
@Service
public class ClaimUpdateUseCase extends BaseUpdateUseCase<ClaimApplication,
        ClaimApplicationUpdateRequest, ClaimApplicationTo> {

    /** Маппер заявления-претензии или обращения. */
    private final ClaimApplicationMapper claimApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param applicationBaseMapper    общий маппер заявления
     * @param applicationAccessService сервис доступа к заявлениям
     * @param statusInformService      сервис status_inform
     * @param preQueueCheckService     сервис пре-очередных проверок
     * @param claimApplicationMapper   маппер подтипа
     * @param outboxWriter             запись outbox-сообщений
     */
    public ClaimUpdateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationBaseMapper applicationBaseMapper,
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ClaimApplicationMapper claimApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationBaseMapper, applicationAccessService,
                statusInformService, preQueueCheckService);
        this.claimApplicationMapper = claimApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.CLAIM;
    }

    @Override
    protected void updateExtraFromRequest(ClaimApplicationUpdateRequest request, ClaimApplication entity) {
        claimApplicationMapper.updateFromRequest(request, entity);
    }

    @Override
    protected ClaimApplicationTo mapToTo(ClaimApplication entity, String reason) {
        return claimApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxUpdated(ClaimApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeClaimApplicationReport(entity, OutboxEventType.CLAIM_APPLICATION_UPDATED);
    }
}
