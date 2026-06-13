package ru.kubsu.applicationservice.support;

import ru.kubsu.contracts.dto.service.application.ServiceApplicationCreateRequest;
import ru.kubsu.contracts.enums.service.application.Channel;
import ru.kubsu.contracts.enums.service.application.ServiceActionType;
import ru.kubsu.contracts.enums.service.application.ServiceType;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Тестовые HTTP-запросы для заявлений на банковские услуги.
 */
public final class ServiceApplicationRequestFixtures {

    private ServiceApplicationRequestFixtures() {
    }

    /**
     * Валидный запрос на создание заявления на банковскую услугу.
     *
     * @return запрос создания
     */
    public static ServiceApplicationCreateRequest validCreateRequest() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        ServiceApplicationCreateRequest request = new ServiceApplicationCreateRequest();
        request.setChannel(Channel.ONLINE);
        request.setLastName("Сидоров-" + suffix);
        request.setFirstName("Сидор");
        request.setPatronymic("Сидорович");
        request.setBirthDate(LocalDate.parse("1988-03-10"));
        request.setGender(Gender.MALE);
        request.setIdentityDocumentSeries("4030");
        request.setIdentityDocumentNumber("111222");
        request.setItn("770123456789");
        request.setInsuranceNumber("12345678901");
        request.setServiceType(ServiceType.SMS_INFO);
        request.setServiceName("SMS-информирование");
        request.setActionType(ServiceActionType.REQUEST);
        return request;
    }
}
