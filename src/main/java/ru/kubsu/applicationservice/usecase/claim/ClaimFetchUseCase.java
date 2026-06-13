package ru.kubsu.applicationservice.usecase.claim;

import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.mapper.ClaimApplicationMapper;
import ru.kubsu.applicationservice.query.ClaimApplicationPredicateBuilder;
import ru.kubsu.applicationservice.repository.ApplicationRepository;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.applicationservice.usecase.base.BaseFetchUseCase;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationQueryParams;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationTo;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Сценарий поиска заявлений-претензий и обращений.
 */
@Service
public class ClaimFetchUseCase extends BaseFetchUseCase<ClaimApplication,
        ClaimApplicationQueryParams, ClaimApplicationTo> {

    /** Маппер заявления-претензии или обращения. */
    private final ClaimApplicationMapper claimApplicationMapper;

    /**
     * @param applicationRepository  репозиторий заявлений
     * @param statusInformService    сервис status_inform
     * @param claimApplicationMapper маппер подтипа
     */
    public ClaimFetchUseCase(
            ApplicationRepository applicationRepository,
            StatusInformService statusInformService,
            ClaimApplicationMapper claimApplicationMapper) {
        super(applicationRepository, statusInformService);
        this.claimApplicationMapper = claimApplicationMapper;
    }

    @Override
    protected ApplicationType getApplicationType() {
        return ApplicationType.CLAIM;
    }

    @Override
    protected Predicate buildPredicate(ClaimApplicationQueryParams params) {
        return ClaimApplicationPredicateBuilder.build(params);
    }

    @Override
    protected ClaimApplicationTo mapToTo(ClaimApplication entity, String reason) {
        return claimApplicationMapper.toTo(entity, reason);
    }
}
