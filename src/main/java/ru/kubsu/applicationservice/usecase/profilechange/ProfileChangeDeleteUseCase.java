package ru.kubsu.applicationservice.usecase.profilechange;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.usecase.base.BaseDeleteUseCase;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий мягкого удаления заявления на изменение данных клиента.
 */
@Service
public class ProfileChangeDeleteUseCase extends BaseDeleteUseCase<ProfileChangeApplication> {

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param applicationAccessService сервис доступа к заявлениям
     * @param outboxWriter             запись outbox-сообщений
     */
    public ProfileChangeDeleteUseCase(
            ApplicationRepository applicationRepository,
            ApplicationAccessService applicationAccessService,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationAccessService, outboxWriter);
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PROFILE_CHANGE;
    }

    @Override
    protected void writeSubtypeOutboxDeleted(ProfileChangeApplication entity) {
        outboxWriter.writeProfileChangeApplicationReport(entity, OutboxEventType.PROFILE_CHANGE_APPLICATION_UPDATED);
    }
}
