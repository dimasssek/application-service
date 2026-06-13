package ru.kubsu.applicationservice.usecase.claim;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ClaimApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseCreateUseCase;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий создания заявления-претензии или обращения.
 */
@Service
public class ClaimCreateUseCase extends BaseCreateUseCase<ClaimApplication,
        ClaimApplicationCreateRequest, ClaimApplicationTo> {

    /** Маппер заявления-претензии или обращения. */
    private final ClaimApplicationMapper claimApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationNumberGenerator генератор номера заявления
     * @param applicationBaseMapper      общий маппер заявления
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param claimApplicationMapper     маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public ClaimCreateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationNumberGenerator applicationNumberGenerator,
            ApplicationBaseMapper applicationBaseMapper,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ClaimApplicationMapper claimApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationNumberGenerator, applicationBaseMapper,
                statusInformService, preQueueCheckService);
        this.claimApplicationMapper = claimApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ClaimApplication createEmptyEntity() {
        return new ClaimApplication();
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.CLAIM;
    }

    @Override
    protected void fillExtraFromCreateRequest(ClaimApplicationCreateRequest request, ClaimApplication entity) {
        claimApplicationMapper.fillFromCreate(request, entity);
    }

    @Override
    protected ClaimApplicationTo mapToTo(ClaimApplication entity, String reason) {
        return claimApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxCreated(ClaimApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeClaimApplicationReport(entity, OutboxEventType.CLAIM_APPLICATION_CREATED);
    }
}
