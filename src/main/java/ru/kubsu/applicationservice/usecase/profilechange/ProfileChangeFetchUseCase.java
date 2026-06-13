package ru.kubsu.applicationservice.usecase.profilechange;

import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.mapper.ProfileChangeApplicationMapper;
import ru.kubsu.applicationservice.query.ProfileChangeApplicationPredicateBuilder;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchUseCase;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий поиска заявлений на изменение данных клиента.
 */
@Service
public class ProfileChangeFetchUseCase extends BaseFetchUseCase<ProfileChangeApplication,
        ProfileChangeApplicationQueryParams, ProfileChangeApplicationTo> {

    /** Маппер заявления на изменение данных клиента. */
    private final ProfileChangeApplicationMapper profilechangeApplicationMapper;

    /**
     * @param applicationRepository          репозиторий заявлений
     * @param statusInformService            сервис status_inform
     * @param profilechangeApplicationMapper маппер подтипа
     */
    public ProfileChangeFetchUseCase(
            ApplicationRepository applicationRepository,
            StatusInformService statusInformService,
            ProfileChangeApplicationMapper profilechangeApplicationMapper) {
        super(applicationRepository, statusInformService);
        this.profilechangeApplicationMapper = profilechangeApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.PROFILE_CHANGE;
    }

    @Override
    protected Predicate buildPredicate(ProfileChangeApplicationQueryParams params) {
        return ProfileChangeApplicationPredicateBuilder.build(params);
    }

    @Override
    protected ProfileChangeApplicationTo mapToTo(ProfileChangeApplication entity, String reason) {
        return profilechangeApplicationMapper.toTo(entity, reason);
    }
}
