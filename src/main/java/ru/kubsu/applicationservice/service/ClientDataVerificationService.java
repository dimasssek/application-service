package ru.kubsu.applicationservice.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;

import java.util.Objects;
import java.util.Optional;

/**
 * Сверка ИНН и СНИЛС заявления с витриной клиента после lookup.
 */
@Service
public class ClientDataVerificationService {

    /**
     * Проверяет ИНН и СНИЛС заявления относительно данных клиента.
     *
     * @param application заявление
     * @param client      найденный клиент
     * @return внутренний статус при отклонении или расхождении, иначе пусто
     */
    public Optional<ApplicationStatusInternal> verify(ApplicationEntity application, Client client) {
        Optional<ApplicationStatusInternal> itnStatus = verifyItn(application.getItn(), client.getItn());
        if (itnStatus.isPresent()) {
            return itnStatus;
        }
        return verifyInsuranceNumber(application.getInsuranceNumber(), client.getInsuranceNumber());
    }

    private Optional<ApplicationStatusInternal> verifyItn(String applicationItn, String clientItn) {
        if (!StringUtils.hasText(applicationItn)) {
            return Optional.of(ApplicationStatusInternal.ITN_NOT_REQUIRED);
        }
        if (!Objects.equals(applicationItn, clientItn)) {
            return Optional.of(ApplicationStatusInternal.ITN_NOT_ACTUAL);
        }
        return Optional.empty();
    }

    private Optional<ApplicationStatusInternal> verifyInsuranceNumber(String applicationSnils, String clientSnils) {
        if (!StringUtils.hasText(applicationSnils)) {
            return Optional.of(ApplicationStatusInternal.INSURANCE_NUMBER_NOT_REQUIRED);
        }
        if (!Objects.equals(applicationSnils, clientSnils)) {
            return Optional.of(ApplicationStatusInternal.INSURANCE_NUMBER_NOT_ACTUAL);
        }
        return Optional.empty();
    }
}
