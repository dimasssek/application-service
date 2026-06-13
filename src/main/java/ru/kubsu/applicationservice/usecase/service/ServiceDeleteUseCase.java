package ru.kubsu.applicationservice.usecase.service;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.usecase.base.BaseDeleteUseCase;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий мягкого удаления заявления на банковскую услугу.
 */
@Service
public class ServiceDeleteUseCase extends BaseDeleteUseCase<ServiceApplication> {

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param applicationAccessService сервис доступа к заявлениям
     * @param outboxWriter             запись outbox-сообщений
     */
    public ServiceDeleteUseCase(
            ApplicationRepository applicationRepository,
            ApplicationAccessService applicationAccessService,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationAccessService, outboxWriter);
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.SERVICE;
    }

    @Override
    protected void writeSubtypeOutboxDeleted(ServiceApplication entity) {
        outboxWriter.writeServiceApplicationReport(entity, OutboxEventType.SERVICE_APPLICATION_UPDATED);
    }
}
