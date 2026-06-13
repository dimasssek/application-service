package ru.kubsu.applicationservice.flc;

import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Проверка даты подачи заявления: не может быть позже текущего момента.
 */
@Component
public class FlcInvalidSubmissionRule implements FlcRule {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ApplicationStatusInternal> check(ApplicationEntity entity, ApplicationType applicationType) {
        if (entity.getCreatedDate() != null && entity.getCreatedDate().isAfter(OffsetDateTime.now())) {
            return Optional.of(ApplicationStatusInternal.FLC_INVALID_SUBMISSION);
        }
        return Optional.empty();
    }
}
