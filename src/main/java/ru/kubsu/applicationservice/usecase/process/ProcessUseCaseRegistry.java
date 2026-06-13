package ru.kubsu.applicationservice.usecase.process;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.UUID;

/**
 * Маршрутизация post-processing заявления по типу.
 */
@Component
@RequiredArgsConstructor
public class ProcessUseCaseRegistry {

    /** Post-processing продуктового заявления. */
    private final ProductProcessUseCase productProcessUseCase;

    /** Post-processing заявления на услугу. */
    private final ServiceProcessUseCase serviceProcessUseCase;

    /** Post-processing заявления на изменение профиля. */
    private final ProfileChangeProcessUseCase profileChangeProcessUseCase;

    /** Post-processing заявления на документ. */
    private final DocumentProcessUseCase documentProcessUseCase;

    /** Post-processing заявления на претензию. */
    private final ClaimProcessUseCase claimProcessUseCase;

    /** Post-processing заявления на полномочия. */
    private final AuthorityProcessUseCase authorityProcessUseCase;

    /**
     * Обрабатывает одно заявление указанного типа.
     *
     * @param applicationType тип заявления
     * @param applicationId   идентификатор заявления
     * @return обработанное заявление
     */
    public ApplicationEntity process(ApplicationType applicationType, UUID applicationId) {
        return switch (applicationType) {
            case PRODUCT -> productProcessUseCase.process(applicationId);
            case SERVICE -> serviceProcessUseCase.process(applicationId);
            case PROFILE_CHANGE -> profileChangeProcessUseCase.process(applicationId);
            case DOCUMENT -> documentProcessUseCase.process(applicationId);
            case CLAIM -> claimProcessUseCase.process(applicationId);
            case AUTHORITY -> authorityProcessUseCase.process(applicationId);
        };
    }
}
