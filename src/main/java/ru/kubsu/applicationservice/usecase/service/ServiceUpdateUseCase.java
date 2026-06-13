package ru.kubsu.applicationservice.usecase.service;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ServiceApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseUpdateUseCase;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationTo;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий обновления заявления на банковскую услугу.
 */
@Service
public class ServiceUpdateUseCase extends BaseUpdateUseCase<ServiceApplication, ServiceApplicationUpdateRequest, ServiceApplicationTo> {

    /** Маппер заявления на банковскую услугу. */
    private final ServiceApplicationMapper serviceApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationBaseMapper      общий маппер заявления
     * @param applicationAccessService   сервис доступа к заявлениям
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param serviceApplicationMapper   маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public ServiceUpdateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationBaseMapper applicationBaseMapper,
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ServiceApplicationMapper serviceApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationBaseMapper, applicationAccessService,
                statusInformService, preQueueCheckService);
        this.serviceApplicationMapper = serviceApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.SERVICE;
    }

    @Override
    protected void updateExtraFromRequest(ServiceApplicationUpdateRequest request, ServiceApplication entity) {
        serviceApplicationMapper.updateFromRequest(request, entity);
    }

    @Override
    protected ServiceApplicationTo mapToTo(ServiceApplication entity, String reason) {
        return serviceApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxUpdated(ServiceApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_UPDATED);
        outboxWriter.writeServiceApplicationReport(entity, OutboxEventType.SERVICE_APPLICATION_UPDATED);
    }
}
