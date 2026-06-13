package ru.kubsu.applicationservice.support;

import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.applicationservice.entity.ProfileChangeApplication;
import ru.kubsu.applicationservice.entity.ServiceApplication;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.Channel;
import ru.kubsu.contracts.enums.service.application.ClientChangeType;
import ru.kubsu.contracts.enums.service.application.ProductCategory;
import ru.kubsu.contracts.enums.service.application.ServiceActionType;
import ru.kubsu.contracts.enums.service.application.ServiceType;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Тестовые данные заявлений.
 */
public final class ApplicationTestFixtures {

    private ApplicationTestFixtures() {
    }

    /**
     * Создаёт продуктовое заявление для интеграционных тестов.
     *
     * @return продуктовое заявление
     */
    public static ProductApplication validProductApplication() {
        ProductApplication application = new ProductApplication();
        application.setNumber("BSPS-PRO-TEST-1");
        application.setChannel(Channel.ONLINE);
        application.setStatusInternal(ApplicationStatusInternal.IN_QUEUE);
        application.setStatusBusiness(ApplicationStatusBusiness.IN_QUEUE);
        application.setCreatedDate(OffsetDateTime.parse("2026-05-31T10:00:00Z"));
        application.setDeleted(false);
        application.setLastName("Иванов");
        application.setFirstName("Иван");
        application.setPatronymic("Иванович");
        application.setBirthDate(LocalDate.parse("1990-01-15"));
        application.setGender(Gender.MALE);
        application.setIdentityDocumentSeries("4010");
        application.setIdentityDocumentNumber("123456");
        application.setProductCategory(ProductCategory.CREDIT);
        application.setProductName("Потребительский кредит");
        application.setProductCode("CR-001");
        application.setAmount(new BigDecimal("500000.00"));
        application.setTermMonths(36);
        application.setPurpose("Ремонт");
        application.setTariffCode("T-01");
        return application;
    }

    /**
     * Создаёт заявление на изменение профиля для интеграционных тестов.
     *
     * @return заявление на изменение профиля
     */
    public static ProfileChangeApplication validProfileChangeApplication() {
        ProfileChangeApplication application = new ProfileChangeApplication();
        application.setNumber("BSPS-PCH-TEST-1");
        application.setChannel(Channel.BRANCH);
        application.setStatusInternal(ApplicationStatusInternal.IN_QUEUE);
        application.setStatusBusiness(ApplicationStatusBusiness.IN_QUEUE);
        application.setCreatedDate(OffsetDateTime.parse("2026-05-31T11:00:00Z"));
        application.setDeleted(false);
        application.setLastName("Петров");
        application.setFirstName("Пётр");
        application.setPatronymic("Петрович");
        application.setBirthDate(LocalDate.parse("1985-06-20"));
        application.setGender(Gender.MALE);
        application.setIdentityDocumentSeries("4020");
        application.setIdentityDocumentNumber("654321");
        application.setChangeType(ClientChangeType.ADDRESS);
        application.setOldValue("г. Москва, ул. Старая, 1");
        application.setNewValue("г. Москва, ул. Новая, 2");
        return application;
    }

    /**
     * Создаёт заявление на банковскую услугу для интеграционных тестов.
     *
     * @return заявление на банковскую услугу
     */
    public static ServiceApplication validServiceApplication() {
        ServiceApplication application = new ServiceApplication();
        application.setNumber("BSPS-SRV-TEST-1");
        application.setChannel(Channel.ONLINE);
        application.setStatusInternal(ApplicationStatusInternal.IN_QUEUE);
        application.setStatusBusiness(ApplicationStatusBusiness.IN_QUEUE);
        application.setCreatedDate(OffsetDateTime.parse("2026-05-31T12:00:00Z"));
        application.setDeleted(false);
        application.setLastName("Сидоров");
        application.setFirstName("Сидор");
        application.setPatronymic("Сидорович");
        application.setBirthDate(LocalDate.parse("1988-03-10"));
        application.setGender(Gender.MALE);
        application.setIdentityDocumentSeries("4030");
        application.setIdentityDocumentNumber("111222");
        application.setItn("770123456789");
        application.setInsuranceNumber("12345678901");
        application.setServiceType(ServiceType.SMS_INFO);
        application.setServiceName("SMS-информирование");
        application.setActionType(ServiceActionType.REQUEST);
        return application;
    }
}
