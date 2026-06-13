package ru.kubsu.applicationservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.AuthorityApplication;
import ru.kubsu.applicationservice.entity.ClaimApplication;
import ru.kubsu.applicationservice.entity.DocumentApplication;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.applicationservice.service.StatusInformService;
import ru.kubsu.contracts.messaging.service.application.ApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.AuthorityApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.ClaimApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.DocumentApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.ProductApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.ProfileChangeApplicationReportMessage;
import ru.kubsu.contracts.messaging.service.application.ServiceApplicationReportMessage;

/**
 * Маппер outbox-сообщений для report-service.
 */
@Component
@RequiredArgsConstructor
public class ReportMessageMapper {

    /** Сервис status_inform. */
    private final StatusInformService statusInformService;

    /**
     * Преобразует заявление в сообщение для витрины report_application.
     *
     * @param source заявление
     * @return сообщение для report-service
     */
    public ApplicationReportMessage toApplicationReportMessage(ApplicationEntity source) {
        return new ApplicationReportMessage()
                .setId(source.getId())
                .setClientId(source.getClientId())
                .setNumber(source.getNumber())
                .setApplicationType(source.getApplicationType())
                .setChannel(source.getChannel())
                .setStatusBusiness(source.getStatusBusiness())
                .setStatusInternal(source.getStatusInternal())
                .setReason(statusInformService.resolveReason(source, source.getApplicationType()))
                .setLastName(source.getLastName())
                .setFirstName(source.getFirstName())
                .setPatronymic(source.getPatronymic())
                .setBirthDate(source.getBirthDate())
                .setGender(source.getGender())
                .setIdentityDocumentSeries(source.getIdentityDocumentSeries())
                .setIdentityDocumentNumber(source.getIdentityDocumentNumber())
                .setItn(source.getItn())
                .setInsuranceNumber(source.getInsuranceNumber())
                .setCreatedDate(source.getCreatedDate())
                .setClosedDate(source.getClosedDate())
                .setDeleted(source.isDeleted());
    }

    /**
     * Преобразует продуктовое заявление в сообщение для витрины report_product_application.
     *
     * @param source продуктовое заявление
     * @return сообщение для report-service
     */
    public ProductApplicationReportMessage toProductApplicationReportMessage(ProductApplication source) {
        return new ProductApplicationReportMessage()
                .setApplicationId(source.getId())
                .setProductCategory(source.getProductCategory())
                .setProductCode(source.getProductCode())
                .setProductName(source.getProductName())
                .setAmount(source.getAmount())
                .setTermMonths(source.getTermMonths())
                .setPurpose(source.getPurpose())
                .setTariffCode(source.getTariffCode());
    }

    /**
     * Преобразует заявление на изменение профиля в сообщение для витрины report_profile_change_application.
     *
     * @param source заявление на изменение профиля
     * @return сообщение для report-service
     */
    public ProfileChangeApplicationReportMessage toProfileChangeApplicationReportMessage(
            ProfileChangeApplication source) {
        return new ProfileChangeApplicationReportMessage()
                .setApplicationId(source.getId())
                .setChangeType(source.getChangeType())
                .setOldValue(source.getOldValue())
                .setNewValue(source.getNewValue());
    }

    /**
     * Преобразует заявление на банковскую услугу в сообщение для витрины report_service_application.
     *
     * @param source заявление на банковскую услугу
     * @return сообщение для report-service
     */
    public ServiceApplicationReportMessage toServiceApplicationReportMessage(ServiceApplication source) {
        return new ServiceApplicationReportMessage()
                .setApplicationId(source.getId())
                .setServiceType(source.getServiceType())
                .setServiceName(source.getServiceName())
                .setActionType(source.getActionType());
    }

    /**
     * Преобразует заявление на получение документа в сообщение для витрины report_document_application.
     *
     * @param source заявление на получение документа
     * @return сообщение для report-service
     */
    public DocumentApplicationReportMessage toDocumentApplicationReportMessage(DocumentApplication source) {
        return new DocumentApplicationReportMessage()
                .setApplicationId(source.getId())
                .setDocumentType(source.getDocumentType())
                .setDeliveryType(source.getDeliveryType())
                .setPurpose(source.getPurpose());
    }

    /**
     * Преобразует заявление-претензию в сообщение для витрины report_claim_application.
     *
     * @param source заявление-претензия
     * @return сообщение для report-service
     */
    public ClaimApplicationReportMessage toClaimApplicationReportMessage(ClaimApplication source) {
        return new ClaimApplicationReportMessage()
                .setApplicationId(source.getId())
                .setClaimType(source.getClaimType())
                .setClaimSubject(source.getClaimSubject())
                .setDescription(source.getDescription())
                .setExternalRef(source.getExternalRef());
    }

    /**
     * Преобразует заявление на полномочия в сообщение для витрины report_authority_application.
     *
     * @param source заявление на полномочия
     * @return сообщение для report-service
     */
    public AuthorityApplicationReportMessage toAuthorityApplicationReportMessage(AuthorityApplication source) {
        return new AuthorityApplicationReportMessage()
                .setApplicationId(source.getId())
                .setAuthorityType(source.getAuthorityType())
                .setPersonName(source.getPersonName())
                .setDocumentNumber(source.getDocumentNumber())
                .setValidUntil(source.getValidUntil());
    }
}
