package ru.kubsu.applicationservice.support;

import ru.kubsu.contracts.dto.service.application.ProductApplicationCreateRequest;
import ru.kubsu.contracts.dto.service.application.ProductApplicationUpdateRequest;
import ru.kubsu.contracts.enums.service.application.Channel;
import ru.kubsu.contracts.enums.service.application.ProductCategory;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Тестовые HTTP-запросы для продуктовых заявлений.
 */
public final class ProductApplicationRequestFixtures {

    private ProductApplicationRequestFixtures() {
    }

    /**
     * Валидный запрос на создание продуктового заявления.
     *
     * @return запрос создания
     */
    public static ProductApplicationCreateRequest validCreateRequest() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        ProductApplicationCreateRequest request = new ProductApplicationCreateRequest();
        request.setChannel(Channel.ONLINE);
        request.setLastName("Иванов-" + suffix);
        request.setFirstName("Иван");
        request.setPatronymic("Иванович");
        request.setBirthDate(LocalDate.parse("1990-01-15"));
        request.setGender(Gender.MALE);
        request.setIdentityDocumentSeries("4010");
        request.setIdentityDocumentNumber("123456");
        request.setItn("770123456789");
        request.setInsuranceNumber("12345678901");
        request.setProductCategory(ProductCategory.CREDIT);
        request.setProductCode("CR-" + suffix);
        request.setProductName("Потребительский кредит");
        request.setAmount(new BigDecimal("500000.00"));
        request.setTermMonths(36);
        request.setPurpose("Ремонт");
        return request;
    }

    /**
     * Запрос на обновление продуктового заявления.
     *
     * @return запрос обновления
     */
    public static ProductApplicationUpdateRequest validUpdateRequest() {
        ProductApplicationUpdateRequest request = new ProductApplicationUpdateRequest();
        request.setLastName("Иванов");
        request.setFirstName("Иван");
        request.setPatronymic("Иванович");
        request.setBirthDate(LocalDate.parse("1990-01-15"));
        request.setGender(Gender.MALE);
        request.setIdentityDocumentSeries("4010");
        request.setIdentityDocumentNumber("123456");
        request.setItn("770123456789");
        request.setInsuranceNumber("12345678901");
        request.setProductCategory(ProductCategory.CREDIT);
        request.setProductCode("CR-001");
        request.setProductName("Потребительский кредит (обновлён)");
        request.setAmount(new BigDecimal("600000.00"));
        request.setTermMonths(48);
        request.setPurpose("Ремонт");
        return request;
    }
}
