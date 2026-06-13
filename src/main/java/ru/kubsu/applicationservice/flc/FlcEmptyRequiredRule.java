package ru.kubsu.applicationservice.flc;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.Optional;

/**
 * Проверка обязательных полей заявления (базовых и специфичных для типа).
 */
@Component
public class FlcEmptyRequiredRule implements FlcRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ApplicationStatusInternal> check(ApplicationEntity entity, ApplicationType applicationType) {
        if (!StringUtils.hasText(entity.getLastName())
                || !StringUtils.hasText(entity.getFirstName())
                || entity.getBirthDate() == null
                || entity.getGender() == null
                || !StringUtils.hasText(entity.getIdentityDocumentSeries())
                || entity.getChannel() == null) {
            return Optional.of(ApplicationStatusInternal.FLC_EMPTY_REQUIRED);
        }

        if (!isSubtypeFieldsFilled(entity, applicationType)) {
            return Optional.of(ApplicationStatusInternal.FLC_EMPTY_REQUIRED);
        }

        return Optional.empty();
    }

    private boolean isSubtypeFieldsFilled(ApplicationEntity entity, ApplicationType applicationType) {
        return switch (applicationType) {
            case PRODUCT -> {
                ProductApplication product = (ProductApplication) entity;
                yield product.getProductCategory() != null && StringUtils.hasText(product.getProductName());
            }
            case SERVICE -> {
                ServiceApplication service = (ServiceApplication) entity;
                yield service.getServiceType() != null
                        && StringUtils.hasText(service.getServiceName())
                        && service.getActionType() != null;
            }
            case PROFILE_CHANGE -> {
                ProfileChangeApplication profileChange = (ProfileChangeApplication) entity;
                yield profileChange.getChangeType() != null && StringUtils.hasText(profileChange.getNewValue());
            }
            case DOCUMENT -> {
                DocumentApplication document = (DocumentApplication) entity;
                yield document.getDocumentType() != null;
            }
            case CLAIM -> {
                ClaimApplication claim = (ClaimApplication) entity;
                yield claim.getClaimType() != null && StringUtils.hasText(claim.getClaimSubject());
            }
            case AUTHORITY -> {
                AuthorityApplication authority = (AuthorityApplication) entity;
                yield authority.getAuthorityType() != null && StringUtils.hasText(authority.getPersonName());
            }
        };
    }
}
