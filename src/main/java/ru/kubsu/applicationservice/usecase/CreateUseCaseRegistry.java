package ru.kubsu.applicationservice.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.usecase.authority.AuthorityCreateUseCase;
import ru.kubsu.applicationservice.usecase.claim.ClaimCreateUseCase;
import ru.kubsu.applicationservice.usecase.document.DocumentCreateUseCase;
import ru.kubsu.applicationservice.usecase.product.ProductCreateUseCase;
import ru.kubsu.applicationservice.usecase.profilechange.ProfileChangeCreateUseCase;
import ru.kubsu.applicationservice.usecase.service.ServiceCreateUseCase;
import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ApplicationTo;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * Маршрутизация создания заявления по типу.
 */
@Component
@RequiredArgsConstructor
public class CreateUseCaseRegistry {

    /** Сценарий создания продуктового заявления. */
    private final ProductCreateUseCase productCreateUseCase;

    /** Сценарий создания заявления на услугу. */
    private final ServiceCreateUseCase serviceCreateUseCase;

    /** Сценарий создания заявления на изменение профиля. */
    private final ProfileChangeCreateUseCase profileChangeCreateUseCase;

    /** Сценарий создания заявления на документ. */
    private final DocumentCreateUseCase documentCreateUseCase;

    /** Сценарий создания заявления на претензию. */
    private final ClaimCreateUseCase claimCreateUseCase;

    /** Сценарий создания заявления на полномочия. */
    private final AuthorityCreateUseCase authorityCreateUseCase;

    /**
     * Создаёт заявление указанного типа.
     *
     * @param applicationType тип заявления
     * @param request         запрос создания
     * @return созданное заявление
     */
    public ApplicationTo create(ApplicationType applicationType, ApplicationCreateRequest request) {
        return switch (applicationType) {
            case PRODUCT -> productCreateUseCase.create((ProductApplicationCreateRequest) request);
            case SERVICE -> serviceCreateUseCase.create((ServiceApplicationCreateRequest) request);
            case PROFILE_CHANGE -> profileChangeCreateUseCase.create((ProfileChangeApplicationCreateRequest) request);
            case DOCUMENT -> documentCreateUseCase.create((DocumentApplicationCreateRequest) request);
            case CLAIM -> claimCreateUseCase.create((ClaimApplicationCreateRequest) request);
            case AUTHORITY -> authorityCreateUseCase.create((AuthorityApplicationCreateRequest) request);
        };
    }
}
