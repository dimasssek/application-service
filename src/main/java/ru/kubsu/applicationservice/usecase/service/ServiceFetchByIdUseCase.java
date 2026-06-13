package ru.kubsu.applicationservice.usecase.service;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.mapper.ServiceApplicationMapper;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchByIdUseCase;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий получения заявления на банковскую услугу по идентификатору.
 */
@Service
public class ServiceFetchByIdUseCase extends BaseFetchByIdUseCase<ServiceApplication, ServiceApplicationTo> {

    /** Маппер заявления на банковскую услугу. */
    private final ServiceApplicationMapper serviceApplicationMapper;

    /**
     * @param applicationAccessService сервис доступа к заявлениям
     * @param statusInformService      сервис status_inform
     * @param serviceApplicationMapper маппер подтипа
     */
    public ServiceFetchByIdUseCase(
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            ServiceApplicationMapper serviceApplicationMapper) {
        super(applicationAccessService, statusInformService);
        this.serviceApplicationMapper = serviceApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.SERVICE;
    }

    @Override
    protected ServiceApplicationTo mapToTo(ServiceApplication entity, String reason) {
        return serviceApplicationMapper.toTo(entity, reason);
    }
}
