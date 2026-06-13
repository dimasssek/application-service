package ru.kubsu.applicationservice.usecase.profilechange;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ProfileChangeApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseUpdateUseCase;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationTo;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий обновления заявления на изменение данных клиента.
 */
@Service
public class ProfileChangeUpdateUseCase extends BaseUpdateUseCase<ProfileChangeApplication,
        ProfileChangeApplicationUpdateRequest, ProfileChangeApplicationTo> {

    /** Маппер заявления на изменение данных клиента. */
    private final ProfileChangeApplicationMapper profilechangeApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository          репозиторий заявлений
     * @param applicationBaseMapper          общий маппер заявления
     * @param applicationAccessService       сервис доступа к заявлениям
     * @param statusInformService            сервис status_inform
     * @param preQueueCheckService           сервис пре-очередных проверок
     * @param profilechangeApplicationMapper маппер подтипа
     * @param outboxWriter                   запись outbox-сообщений
     */
    public ProfileChangeUpdateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationBaseMapper applicationBaseMapper,
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ProfileChangeApplicationMapper profilechangeApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationBaseMapper, applicationAccessService,
                statusInformService, preQueueCheckService);
        this.profilechangeApplicationMapper = profilechangeApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PROFILE_CHANGE;
    }

    @Override
    protected void updateExtraFromRequest(ProfileChangeApplicationUpdateRequest request,
                                          ProfileChangeApplication entity) {
        profilechangeApplicationMapper.updateFromRequest(request, entity);
    }

    @Override
    protected ProfileChangeApplicationTo mapToTo(ProfileChangeApplication entity, String reason) {
        return profilechangeApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxUpdated(ProfileChangeApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeProfileChangeApplicationReport(entity, OutboxEventType.PROFILE_CHANGE_APPLICATION_UPDATED);
    }
}
