package ru.kubsu.applicationservice.usecase.service;

import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.mapper.ServiceApplicationMapper;
import ru.kubsu.applicationservice.query.ServiceApplicationPredicateBuilder;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchUseCase;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий поиска заявлений на банковские услуги.
 */
@Service
public class ServiceFetchUseCase extends BaseFetchUseCase<ServiceApplication, ServiceApplicationQueryParams, ServiceApplicationTo> {

    /** Маппер заявления на банковскую услугу. */
    private final ServiceApplicationMapper serviceApplicationMapper;

    /**
     * @param applicationRepository    репозиторий заявлений
     * @param statusInformService      сервис status_inform
     * @param serviceApplicationMapper маппер подтипа
     */
    public ServiceFetchUseCase(
            ApplicationRepository applicationRepository,
            StatusInformService statusInformService,
            ServiceApplicationMapper serviceApplicationMapper) {
        super(applicationRepository, statusInformService);
        this.serviceApplicationMapper = serviceApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.SERVICE;
    }

    @Override
    protected Predicate buildPredicate(ServiceApplicationQueryParams params) {
        return ServiceApplicationPredicateBuilder.build(params);
    }

    @Override
    protected ServiceApplicationTo mapToTo(ServiceApplication entity, String reason) {
        return serviceApplicationMapper.toTo(entity, reason);
    }
}
