package ru.kubsu.applicationservice.usecase.service;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.mapper.ApplicationBaseMapper;
import ru.kubsu.applicationservice.mapper.ServiceApplicationMapper;
import ru.kubsu.applicationservice.outbox.OutboxWriter;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.ApplicationNumberGenerator;
import ru.kubsu.applicationservice.service.PreQueueCheckService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseCreateUseCase;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.OutboxEventType;

/**
 * Сценарий создания заявления на банковскую услугу.
 */
@Service
public class ServiceCreateUseCase extends BaseCreateUseCase<ServiceApplication, ServiceApplicationCreateRequest, ServiceApplicationTo> {

    /** Маппер заявления на банковскую услугу. */
    private final ServiceApplicationMapper serviceApplicationMapper;

    /** Запись outbox-сообщений. */
    private final OutboxWriter outboxWriter;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param applicationNumberGenerator генератор номера заявления
     * @param applicationBaseMapper      общий маппер заявления
     * @param statusInformService        сервис status_inform
     * @param preQueueCheckService       сервис пре-очередных проверок
     * @param serviceApplicationMapper   маппер подтипа
     * @param outboxWriter               запись outbox-сообщений
     */
    public ServiceCreateUseCase(
            ApplicationRepository applicationRepository,
            ApplicationNumberGenerator applicationNumberGenerator,
            ApplicationBaseMapper applicationBaseMapper,
            StatusInformService statusInformService,
            PreQueueCheckService preQueueCheckService,
            ServiceApplicationMapper serviceApplicationMapper,
            OutboxWriter outboxWriter) {
        super(applicationRepository, applicationNumberGenerator, applicationBaseMapper,
                statusInformService, preQueueCheckService);
        this.serviceApplicationMapper = serviceApplicationMapper;
        this.outboxWriter = outboxWriter;
    }

    @Override
    protected ServiceApplication createEmptyEntity() {
        return new ServiceApplication();
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.SERVICE;
    }

    @Override
    protected void fillExtraFromCreateRequest(ServiceApplicationCreateRequest request, ServiceApplication entity) {
        serviceApplicationMapper.fillFromCreate(request, entity);
    }

    @Override
    protected ServiceApplicationTo mapToTo(ServiceApplication entity, String reason) {
        return serviceApplicationMapper.toTo(entity, reason);
    }

    @Override
    protected void writeOutboxCreated(ServiceApplication entity) {
        outboxWriter.writeApplicationReport(entity, OutboxEventType.APPLICATION_CREATED);
        outboxWriter.writeServiceApplicationReport(entity, OutboxEventType.SERVICE_APPLICATION_CREATED);
    }
}
