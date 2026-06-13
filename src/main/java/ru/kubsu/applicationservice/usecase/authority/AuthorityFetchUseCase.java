package ru.kubsu.applicationservice.usecase.authority;

import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.mapper.AuthorityApplicationMapper;
import ru.kubsu.applicationservice.query.AuthorityApplicationPredicateBuilder;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchUseCase;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий поиска заявлений на полномочия и доступы.
 */
@Service
public class AuthorityFetchUseCase extends BaseFetchUseCase<AuthorityApplication,
        AuthorityApplicationQueryParams, AuthorityApplicationTo> {

    /** Маппер заявления на полномочия и доступы. */
    private final AuthorityApplicationMapper authorityApplicationMapper;

    /**
     * @param applicationRepository      репозиторий заявлений
     * @param statusInformService        сервис status_inform
     * @param authorityApplicationMapper маппер подтипа
     */
    public AuthorityFetchUseCase(
            ApplicationRepository applicationRepository,
            StatusInformService statusInformService,
            AuthorityApplicationMapper authorityApplicationMapper) {
        super(applicationRepository, statusInformService);
        this.authorityApplicationMapper = authorityApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.AUTHORITY;
    }

    @Override
    protected Predicate buildPredicate(AuthorityApplicationQueryParams params) {
        return AuthorityApplicationPredicateBuilder.build(params);
    }

    @Override
    protected AuthorityApplicationTo mapToTo(AuthorityApplication entity, String reason) {
        return authorityApplicationMapper.toTo(entity, reason);
    }
}
