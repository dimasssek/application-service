package ru.kubsu.applicationservice.support;

import ru.kubsu.contracts.dto.service.application.ApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.AuthorityType;
import ru.kubsu.contracts.enums.service.application.Channel;
import ru.kubsu.contracts.enums.service.application.ClaimType;
import ru.kubsu.contracts.enums.service.application.ClientChangeType;
import ru.kubsu.contracts.enums.service.application.DocumentDeliveryType;
import ru.kubsu.contracts.enums.service.application.DocumentType;
import ru.kubsu.contracts.enums.service.application.ProductCategory;
import ru.kubsu.contracts.enums.service.application.ServiceActionType;
import ru.kubsu.contracts.enums.service.application.ServiceType;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Валидные запросы создания заявлений для интеграционных тестов.
 */
public final class ApplicationCreateRequestFixtures {

    private ApplicationCreateRequestFixtures() {
    }

    /**
     * @param applicationType тип заявления
     * @return запрос создания с уникальными данными клиента
     */
    public static ApplicationCreateRequest validCreateRequest(ApplicationType applicationType) {
        return switch (applicationType) {
            case PRODUCT -> productCreateRequest();
            case SERVICE -> serviceCreateRequest();
            case PROFILE_CHANGE -> profileChangeCreateRequest();
            case DOCUMENT -> documentCreateRequest();
            case CLAIM -> claimCreateRequest();
            case AUTHORITY -> authorityCreateRequest();
        };
    }

    /**
     * @param applicationType тип заявления
     * @return REST-путь создания заявления
     */
    public static String createPath(ApplicationType applicationType) {
        return switch (applicationType) {
            case PRODUCT -> "/product";
            case SERVICE -> "/service";
            case PROFILE_CHANGE -> "/profile-change";
            case DOCUMENT -> "/document";
            case CLAIM -> "/claim";
            case AUTHORITY -> "/authority";
        };
    }

    private static ProductApplicationCreateRequest productCreateRequest() {
        String suffix = uniqueSuffix();
        ProductApplicationCreateRequest request = new ProductApplicationCreateRequest();
        fillBaseClientFields(request, suffix);
        request.setChannel(Channel.ONLINE);
        request.setProductCategory(ProductCategory.CREDIT);
        request.setProductCode("CR-" + suffix);
        request.setProductName("Потребительский кредит");
        request.setAmount(new BigDecimal("500000.00"));
        request.setTermMonths(36);
        request.setPurpose("Ремонт");
        return request;
    }

    private static ServiceApplicationCreateRequest serviceCreateRequest() {
        String suffix = uniqueSuffix();
        ServiceApplicationCreateRequest request = new ServiceApplicationCreateRequest();
        fillBaseClientFields(request, suffix);
        request.setChannel(Channel.ONLINE);
        request.setServiceType(ServiceType.SMS_INFO);
        request.setServiceName("SMS-информирование");
        request.setActionType(ServiceActionType.REQUEST);
        return request;
    }

    private static ProfileChangeApplicationCreateRequest profileChangeCreateRequest() {
        String suffix = uniqueSuffix();
        ProfileChangeApplicationCreateRequest request = new ProfileChangeApplicationCreateRequest();
        fillBaseClientFields(request, suffix);
        request.setChannel(Channel.BRANCH);
        request.setChangeType(ClientChangeType.ADDRESS);
        request.setOldValue("г. Москва, ул. Старая, 1");
        request.setNewValue("г. Москва, ул. Новая, 2");
        return request;
    }

    private static DocumentApplicationCreateRequest documentCreateRequest() {
        String suffix = uniqueSuffix();
        DocumentApplicationCreateRequest request = new DocumentApplicationCreateRequest();
        fillBaseClientFields(request, suffix);
        request.setChannel(Channel.ONLINE);
        request.setDocumentType(DocumentType.ACCOUNT_STATEMENT);
        request.setDeliveryType(DocumentDeliveryType.BRANCH);
        request.setPurpose("Для предоставления по месту работы");
        return request;
    }

    private static ClaimApplicationCreateRequest claimCreateRequest() {
        String suffix = uniqueSuffix();
        ClaimApplicationCreateRequest request = new ClaimApplicationCreateRequest();
        fillBaseClientFields(request, suffix);
        request.setChannel(Channel.ONLINE);
        request.setClaimType(ClaimType.COMPLAINT);
        request.setClaimSubject("Некорректное списание");
        request.setDescription("Списание без согласия клиента");
        request.setExternalRef("OP-" + suffix);
        return request;
    }

    private static AuthorityApplicationCreateRequest authorityCreateRequest() {
        String suffix = uniqueSuffix();
        AuthorityApplicationCreateRequest request = new AuthorityApplicationCreateRequest();
        fillBaseClientFields(request, suffix);
        request.setChannel(Channel.BRANCH);
        request.setAuthorityType(AuthorityType.POA_ACCOUNT_ACCESS);
        request.setPersonName("Петров Пётр Петрович");
        request.setDocumentNumber("Доверенность-" + suffix);
        request.setValidUntil(LocalDate.parse("2027-12-31"));
        return request;
    }

    private static void fillBaseClientFields(ApplicationCreateRequest request, String suffix) {
        request.setLastName("Иванов-" + suffix);
        request.setFirstName("Иван");
        request.setPatronymic("Иванович");
        request.setBirthDate(LocalDate.parse("1990-01-15"));
        request.setGender(Gender.MALE);
        request.setIdentityDocumentSeries("4010");
        request.setIdentityDocumentNumber("123456");
        request.setItn("770123456789");
        request.setInsuranceNumber("12345678901");
    }

    private static String uniqueSuffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
