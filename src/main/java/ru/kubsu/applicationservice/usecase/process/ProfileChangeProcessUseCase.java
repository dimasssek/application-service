package ru.kubsu.applicationservice.usecase.process;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ProfileChangeApplicationRepository;
import ru.kubsu.applicationservice.service.ClientDataVerificationService;
import ru.kubsu.applicationservice.service.ClientLookupService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Post-processing заявления на изменение профиля в general-check.
 */
@Service
public class ProfileChangeProcessUseCase extends BaseProcessUseCase<ProfileChangeApplication> {

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param typedRepository     репозиторий заявлений на изменение профиля
     * @param clientLookupService сервис поиска клиента
     * @param statusInformService сервис status_inform
     * @param outboxWriter        запись outbox-сообщений
     */
    public ProfileChangeProcessUseCase(ProfileChangeApplicationRepository typedRepository,
                                       ClientLookupService clientLookupService,
                                       ClientDataVerificationService clientDataVerificationService,
                                       StatusInformService statusInformService,
                                       OutboxWriter outboxWriter) {
        super(typedRepository, clientLookupService, clientDataVerificationService, statusInformService);
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PROFILE_CHANGE;
    }

    @Override
    protected void writeOutboxUpdated(ProfileChangeApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeProfileChangeApplicationReport(entity, OutboxEventType.PROFILE_CHANGE_APPLICATION_UPDATED);
    }
}
