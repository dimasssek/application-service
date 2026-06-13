package ru.kubsu.applicationservice.usecase.claim;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.mapper.ClaimApplicationMapper;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchByIdUseCase;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий получения заявления-претензии или обращения по идентификатору.
 */
@Service
public class ClaimFetchByIdUseCase extends BaseFetchByIdUseCase<ClaimApplication, ClaimApplicationTo> {

    /** Маппер заявления-претензии или обращения. */
    private final ClaimApplicationMapper claimApplicationMapper;

    /**
     * @param applicationAccessService сервис доступа к заявлениям
     * @param statusInformService      сервис status_inform
     * @param claimApplicationMapper   маппер подтипа
     */
    public ClaimFetchByIdUseCase(
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            ClaimApplicationMapper claimApplicationMapper) {
        super(applicationAccessService, statusInformService);
        this.claimApplicationMapper = claimApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.CLAIM;
    }

    @Override
    protected ClaimApplicationTo mapToTo(ClaimApplication entity, String reason) {
        return claimApplicationMapper.toTo(entity, reason);
    }
}
