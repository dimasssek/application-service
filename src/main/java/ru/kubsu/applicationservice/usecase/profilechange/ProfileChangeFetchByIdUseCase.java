package ru.kubsu.applicationservice.usecase.profilechange;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.mapper.ProfileChangeApplicationMapper;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchByIdUseCase;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий получения заявления на изменение данных клиента по идентификатору.
 */
@Service
public class ProfileChangeFetchByIdUseCase extends BaseFetchByIdUseCase<ProfileChangeApplication,
        ProfileChangeApplicationTo> {

    /** Маппер заявления на изменение данных клиента. */
    private final ProfileChangeApplicationMapper profilechangeApplicationMapper;

    /**
     * @param applicationAccessService       сервис доступа к заявлениям
     * @param statusInformService            сервис status_inform
     * @param profilechangeApplicationMapper маппер подтипа
     */
    public ProfileChangeFetchByIdUseCase(
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            ProfileChangeApplicationMapper profilechangeApplicationMapper) {
        super(applicationAccessService, statusInformService);
        this.profilechangeApplicationMapper = profilechangeApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PROFILE_CHANGE;
    }

    @Override
    protected ProfileChangeApplicationTo mapToTo(ProfileChangeApplication entity, String reason) {
        return profilechangeApplicationMapper.toTo(entity, reason);
    }
}
