package ru.kubsu.applicationservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.util.List;
import java.util.UUID;

/**
 * Сервис проверки дубликатов заявлений среди IN_QUEUE и APPLIED.
 */
@Service
@RequiredArgsConstructor
public class DuplicateCheckService {

    private static final List<ApplicationStatusBusiness> DUPLICATE_STATUSES = List.of(
            ApplicationStatusBusiness.IN_QUEUE,
            ApplicationStatusBusiness.APPLIED);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Проверяет наличие дубликата заявления.
     *
     * @param candidate       проверяемое заявление
     * @param applicationType тип заявления
     * @param excludeId       идентификатор заявления, исключаемый из проверки (для update)
     * @return true, если найден дубликат
     */
    public boolean isDuplicate(ApplicationEntity candidate,
                               ApplicationType applicationType,
                               UUID excludeId) {
        Long count = switch (applicationType) {
            case PRODUCT -> countProductDuplicates((ProductApplication) candidate, excludeId);
            case SERVICE -> countServiceDuplicates((ServiceApplication) candidate, excludeId);
            case PROFILE_CHANGE -> countProfileChangeDuplicates((ProfileChangeApplication) candidate, excludeId);
            case DOCUMENT -> countDocumentDuplicates((DocumentApplication) candidate, excludeId);
            case CLAIM -> countClaimDuplicates((ClaimApplication) candidate, excludeId);
            case AUTHORITY -> countAuthorityDuplicates((AuthorityApplication) candidate, excludeId);
        };
        return count != null && count > 0;
    }

    private Long countProductDuplicates(ProductApplication candidate, UUID excludeId) {
        if (StringUtils.hasText(candidate.getProductCode())) {
            return queryCount("""
                    select count(p) from ProductApplication p
                    where p.deleted = false
                      and p.statusBusiness in :statuses
                      and p.lastName = :lastName
                      and p.firstName = :firstName
                      and p.birthDate = :birthDate
                      and p.identityDocumentSeries = :identityDocumentSeries
                      and (:identityDocumentNumber is null or p.identityDocumentNumber = :identityDocumentNumber)
                      and p.productCode = :productCode
                      and (:excludeId is null or p.id <> :excludeId)
                    """, candidate, excludeId)
                    .setParameter("productCode", candidate.getProductCode())
                    .getSingleResult();
        }

        return queryCount("""
                select count(p) from ProductApplication p
                where p.deleted = false
                  and p.statusBusiness in :statuses
                  and p.lastName = :lastName
                  and p.firstName = :firstName
                  and p.birthDate = :birthDate
                  and p.identityDocumentSeries = :identityDocumentSeries
                  and (:identityDocumentNumber is null or p.identityDocumentNumber = :identityDocumentNumber)
                  and p.productName = :productName
                  and (:excludeId is null or p.id <> :excludeId)
                """, candidate, excludeId)
                .setParameter("productName", candidate.getProductName())
                .getSingleResult();
    }

    private Long countServiceDuplicates(ServiceApplication candidate, UUID excludeId) {
        return queryCount("""
                select count(s) from ServiceApplication s
                where s.deleted = false
                  and s.statusBusiness in :statuses
                  and s.lastName = :lastName
                  and s.firstName = :firstName
                  and s.birthDate = :birthDate
                  and s.identityDocumentSeries = :identityDocumentSeries
                  and (:identityDocumentNumber is null or s.identityDocumentNumber = :identityDocumentNumber)
                  and s.serviceType = :serviceType
                  and s.actionType = :actionType
                  and (:excludeId is null or s.id <> :excludeId)
                """, candidate, excludeId)
                .setParameter("serviceType", candidate.getServiceType())
                .setParameter("actionType", candidate.getActionType())
                .getSingleResult();
    }

    private Long countProfileChangeDuplicates(ProfileChangeApplication candidate, UUID excludeId) {
        return queryCount("""
                select count(p) from ProfileChangeApplication p
                where p.deleted = false
                  and p.statusBusiness in :statuses
                  and p.lastName = :lastName
                  and p.firstName = :firstName
                  and p.birthDate = :birthDate
                  and p.identityDocumentSeries = :identityDocumentSeries
                  and (:identityDocumentNumber is null or p.identityDocumentNumber = :identityDocumentNumber)
                  and p.changeType = :changeType
                  and p.newValue = :newValue
                  and (:excludeId is null or p.id <> :excludeId)
                """, candidate, excludeId)
                .setParameter("changeType", candidate.getChangeType())
                .setParameter("newValue", candidate.getNewValue())
                .getSingleResult();
    }

    private Long countDocumentDuplicates(DocumentApplication candidate, UUID excludeId) {
        return queryCount("""
                select count(d) from DocumentApplication d
                where d.deleted = false
                  and d.statusBusiness in :statuses
                  and d.lastName = :lastName
                  and d.firstName = :firstName
                  and d.birthDate = :birthDate
                  and d.identityDocumentSeries = :identityDocumentSeries
                  and (:identityDocumentNumber is null or d.identityDocumentNumber = :identityDocumentNumber)
                  and d.documentType = :documentType
                  and (:excludeId is null or d.id <> :excludeId)
                """, candidate, excludeId)
                .setParameter("documentType", candidate.getDocumentType())
                .getSingleResult();
    }

    private Long countClaimDuplicates(ClaimApplication candidate, UUID excludeId) {
        return queryCount("""
                select count(c) from ClaimApplication c
                where c.deleted = false
                  and c.statusBusiness in :statuses
                  and c.lastName = :lastName
                  and c.firstName = :firstName
                  and c.birthDate = :birthDate
                  and c.identityDocumentSeries = :identityDocumentSeries
                  and (:identityDocumentNumber is null or c.identityDocumentNumber = :identityDocumentNumber)
                  and c.claimType = :claimType
                  and c.claimSubject = :claimSubject
                  and (:excludeId is null or c.id <> :excludeId)
                """, candidate, excludeId)
                .setParameter("claimType", candidate.getClaimType())
                .setParameter("claimSubject", candidate.getClaimSubject())
                .getSingleResult();
    }

    private Long countAuthorityDuplicates(AuthorityApplication candidate, UUID excludeId) {
        return queryCount("""
                select count(a) from AuthorityApplication a
                where a.deleted = false
                  and a.statusBusiness in :statuses
                  and a.lastName = :lastName
                  and a.firstName = :firstName
                  and a.birthDate = :birthDate
                  and a.identityDocumentSeries = :identityDocumentSeries
                  and (:identityDocumentNumber is null or a.identityDocumentNumber = :identityDocumentNumber)
                  and a.authorityType = :authorityType
                  and a.personName = :personName
                  and (:excludeId is null or a.id <> :excludeId)
                """, candidate, excludeId)
                .setParameter("authorityType", candidate.getAuthorityType())
                .setParameter("personName", candidate.getPersonName())
                .getSingleResult();
    }

    private TypedQuery<Long> queryCount(String jpql, ApplicationEntity candidate, UUID excludeId) {
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("statuses", DUPLICATE_STATUSES)
                .setParameter("lastName", candidate.getLastName())
                .setParameter("firstName", candidate.getFirstName())
                .setParameter("birthDate", candidate.getBirthDate())
                .setParameter("identityDocumentSeries", candidate.getIdentityDocumentSeries())
                .setParameter("identityDocumentNumber", candidate.getIdentityDocumentNumber())
                .setParameter("excludeId", excludeId);
    }
}
