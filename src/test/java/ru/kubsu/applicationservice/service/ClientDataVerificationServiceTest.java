package ru.kubsu.applicationservice.service;

import org.junit.jupiter.api.Test;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.applicationservice.entity.ProductApplication;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-тесты сверки ИНН и СНИЛС с витриной клиента.
 */
class ClientDataVerificationServiceTest {

    private final ClientDataVerificationService service = new ClientDataVerificationService();

    @Test
    void shouldReturnEmptyWhenItnAndSnilsMatch() {
        ApplicationEntity application = application("770123456789", "12345678901");
        Client client = client("770123456789", "12345678901");

        assertThat(service.verify(application, client)).isEmpty();
    }

    @Test
    void shouldReturnItnNotRequiredWhenItnMissing() {
        ApplicationEntity application = application(null, "12345678901");
        Client client = client("770123456789", "12345678901");

        assertThat(service.verify(application, client))
                .contains(ApplicationStatusInternal.ITN_NOT_REQUIRED);
    }

    @Test
    void shouldReturnItnNotActualWhenItnMismatch() {
        ApplicationEntity application = application("770123456789", "12345678901");
        Client client = client("999999999999", "12345678901");

        assertThat(service.verify(application, client))
                .contains(ApplicationStatusInternal.ITN_NOT_ACTUAL);
    }

    @Test
    void shouldReturnInsuranceNumberNotRequiredWhenSnilsMissing() {
        ApplicationEntity application = application("770123456789", null);
        Client client = client("770123456789", "12345678901");

        assertThat(service.verify(application, client))
                .contains(ApplicationStatusInternal.INSURANCE_NUMBER_NOT_REQUIRED);
    }

    @Test
    void shouldReturnInsuranceNumberNotActualWhenSnilsMismatch() {
        ApplicationEntity application = application("770123456789", "12345678901");
        Client client = client("770123456789", "99999999999");

        assertThat(service.verify(application, client))
                .contains(ApplicationStatusInternal.INSURANCE_NUMBER_NOT_ACTUAL);
    }

    private ApplicationEntity application(String itn, String insuranceNumber) {
        ProductApplication application = new ProductApplication();
        application.setItn(itn);
        application.setInsuranceNumber(insuranceNumber);
        return application;
    }

    private Client client(String itn, String insuranceNumber) {
        return new Client()
                .setItn(itn)
                .setInsuranceNumber(insuranceNumber);
    }
}
