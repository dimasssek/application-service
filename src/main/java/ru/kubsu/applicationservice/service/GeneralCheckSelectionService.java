package ru.kubsu.applicationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.repository.AuthorityApplicationRepository;
import ru.kubsu.applicationservice.repository.ClaimApplicationRepository;
import ru.kubsu.applicationservice.repository.DocumentApplicationRepository;
import ru.kubsu.applicationservice.repository.ProductApplicationRepository;
import ru.kubsu.applicationservice.repository.ProfileChangeApplicationRepository;
import ru.kubsu.applicationservice.repository.ServiceApplicationRepository;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Сервис выборки заявлений для general-check по типу.
 */
@Service
@RequiredArgsConstructor
public class GeneralCheckSelectionService {

    /** Репозиторий продуктовых заявлений. */
    private final ProductApplicationRepository productApplicationRepository;

    /** Репозиторий заявлений на услуги. */
    private final ServiceApplicationRepository serviceApplicationRepository;

    /** Репозиторий заявлений на изменение профиля. */
    private final ProfileChangeApplicationRepository profileChangeApplicationRepository;

    /** Репозиторий заявлений на документы. */
    private final DocumentApplicationRepository documentApplicationRepository;

    /** Репозиторий заявлений на претензии. */
    private final ClaimApplicationRepository claimApplicationRepository;

    /** Репозиторий заявлений на полномочия. */
    private final AuthorityApplicationRepository authorityApplicationRepository;

    /**
     * Возвращает идентификаторы заявлений для пакетной обработки.
     *
     * @param applicationType тип заявлений
     * @param dateTo          верхняя граница created_date (включительно)
     * @return список идентификаторов
     */
    public List<UUID> findApplicationIds(ApplicationType applicationType, OffsetDateTime dateTo) {
        return switch (applicationType) {
            case PRODUCT -> productApplicationRepository.findForGeneralCheck(dateTo).stream()
                    .map(ApplicationEntity::getId)
                    .toList();
            case SERVICE -> serviceApplicationRepository.findForGeneralCheck(dateTo).stream()
                    .map(ApplicationEntity::getId)
                    .toList();
            case PROFILE_CHANGE -> profileChangeApplicationRepository.findForGeneralCheck(dateTo).stream()
                    .map(ApplicationEntity::getId)
                    .toList();
            case DOCUMENT -> documentApplicationRepository.findForGeneralCheck(dateTo).stream()
                    .map(ApplicationEntity::getId)
                    .toList();
            case CLAIM -> claimApplicationRepository.findForGeneralCheck(dateTo).stream()
                    .map(ApplicationEntity::getId)
                    .toList();
            case AUTHORITY -> authorityApplicationRepository.findForGeneralCheck(dateTo).stream()
                    .map(ApplicationEntity::getId)
                    .toList();
        };
    }
}
