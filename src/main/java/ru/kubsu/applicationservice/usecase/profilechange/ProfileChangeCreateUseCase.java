package ru.kubsu.applicationservice.usecase.profilechange;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ProfileChangeApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseCreateUseCase;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий создания заявления на изменение данных клиента.
 */
@Service
public class ProfileChangeCreateUseCase extends BaseCreateUseCase<ProfileChangeApplication,
        ProfileChangeApplicationCreateRequest, ProfileChangeApplicationTo> {

    /** Маппер заявления на изменение данных клиента. */
    private final ProfileChangeApplicationMapper profilechangeApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository           репозиторий заявлений
     * @param applicationNumberGenerator      генератор номера заявления
     * @param applicationBaseMapper           общий маппер заявления
     * @param statusInformService             сервис status_inform
     * @param preQueueCheckService            сервис пре-очередных проверок
     * @param profilechangeApplicationMapper  маппер подтипа
     * @param outboxWriter                    запись outbox-сообщений
     */
    public ProfileChangeCreateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationNumberGenerator applicationNumberGenerator,
            ApplicationBaseMapper applicationBaseMapper,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ProfileChangeApplicationMapper profilechangeApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationNumberGenerator, applicationBaseMapper,
                statusInformService, preQueueCheckService);
        this.profilechangeApplicationMapper = profilechangeApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ProfileChangeApplication createEmptyEntity() {
        return new ProfileChangeApplication();
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PROFILE_CHANGE;
    }

    @Override
    protected void fillExtraFromCreateRequest(ProfileChangeApplicationCreateRequest request,
                                              ProfileChangeApplication entity) {
        profilechangeApplicationMapper.fillFromCreate(request, entity);
    }

    @Override
    protected ProfileChangeApplicationTo mapToTo(ProfileChangeApplication entity, String reason) {
        return profilechangeApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxCreated(ProfileChangeApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeProfileChangeApplicationReport(entity, OutboxEventType.PROFILE_CHANGE_APPLICATION_CREATED);
    }
}
