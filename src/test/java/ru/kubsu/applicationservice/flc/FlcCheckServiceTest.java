package ru.kubsu.applicationservice.flc;

import org.junit.jupiter.api.Test;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.Channel;
import ru.kubsu.contracts.enums.service.application.ProductCategory;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-тесты ФЛК заявлений.
 */
class FlcCheckServiceTest {

    private final FlcCheckService flcCheckService = new FlcCheckService(
            new FlcRuleFactory(new FlcEmptyRequiredRule(), new FlcInvalidSubmissionRule()));

    /**
     * Пустое обязательное поле должно давать FLC_EMPTY_REQUIRED.
     */
    @Test
    void shouldReturnFlcEmptyRequiredWhenRequiredFieldMissing() {
        ProductApplication application = validProduct();
        application.setLastName(null);

        Optional<ApplicationStatusInternal> result = flcCheckService.check(
                application, ApplicationType.PRODUCT);

        assertThat(result).contains(ApplicationStatusInternal.FLC_EMPTY_REQUIRED);
    }

    /**
     * Дата подачи в будущем должна давать FLC_INVALID_SUBMISSION.
     */
    @Test
    void shouldReturnFlcInvalidSubmissionWhenCreatedDateInFuture() {
        ProductApplication application = validProduct();
        application.setCreatedDate(OffsetDateTime.now().plusDays(1));

        Optional<ApplicationStatusInternal> result = flcCheckService.check(
                application, ApplicationType.PRODUCT);

        assertThat(result).contains(ApplicationStatusInternal.FLC_INVALID_SUBMISSION);
    }

    private ProductApplication validProduct() {
        ProductApplication application = new ProductApplication();
        application.setChannel(Channel.ONLINE);
        application.setCreatedDate(OffsetDateTime.now());
        application.setLastName("Иванов");
        application.setFirstName("Иван");
        application.setBirthDate(LocalDate.parse("1990-01-15"));
        application.setGender(Gender.MALE);
        application.setIdentityDocumentSeries("4010");
        application.setProductCategory(ProductCategory.CREDIT);
        application.setProductName("Кредит");
        return application;
    }
}
