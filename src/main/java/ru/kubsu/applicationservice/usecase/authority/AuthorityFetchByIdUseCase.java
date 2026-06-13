package ru.kubsu.applicationservice.usecase.authority;

import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.mapper.AuthorityApplicationMapper;
import ru.kubsu.applicationservice.service.ApplicationAccessService;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchByIdUseCase;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий получения заявления на полномочия и доступы по идентификатору.
 */
@Service
public class AuthorityFetchByIdUseCase extends BaseFetchByIdUseCase<AuthorityApplication, AuthorityApplicationTo> {

    /** Маппер заявления на полномочия и доступы. */
    private final AuthorityApplicationMapper authorityApplicationMapper;

    /**
     * @param applicationAccessService   сервис доступа к заявлениям
     * @param statusInformService        сервис status_inform
     * @param authorityApplicationMapper маппер подтипа
     */
    public AuthorityFetchByIdUseCase(
            ApplicationAccessService applicationAccessService,
            StatusInformService statusInformService,
            AuthorityApplicationMapper authorityApplicationMapper) {
        super(applicationAccessService, statusInformService);
        this.authorityApplicationMapper = authorityApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.AUTHORITY;
    }

    @Override
    protected AuthorityApplicationTo mapToTo(AuthorityApplication entity, String reason) {
        return authorityApplicationMapper.toTo(entity, reason);
    }
}
