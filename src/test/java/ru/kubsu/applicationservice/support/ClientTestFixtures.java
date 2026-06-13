package ru.kubsu.applicationservice.support;

import ru.kubsu.contracts.dto.service.client.ClientTo;
import ru.kubsu.contracts.enums.service.client.ClientStatus;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Тестовые данные клиента для интеграционных тестов.
 */
public final class ClientTestFixtures {

    private ClientTestFixtures() {
    }

    /**
     * Создаёт {@link ClientTo} для события создания клиента.
     *
     * @param actualDate дата актуализации события
     * @return транспортный объект клиента
     */
    public static ClientTo clientEvent(OffsetDateTime actualDate) {
        return clientEvent(UUID.randomUUID(), actualDate);
    }

    /**
     * Создаёт {@link ClientTo} для события создания клиента.
     *
     * @param clientId   идентификатор клиента
     * @param actualDate дата актуализации события
     * @return транспортный объект клиента
     */
    public static ClientTo clientEvent(UUID clientId, OffsetDateTime actualDate) {
        return new ClientTo()
                .setId(clientId)
                .setLastName("Иванов")
                .setFirstName("Иван")
                .setPatronymic("Иванович")
                .setBirthDate(LocalDate.of(1990, 1, 15))
                .setGender(Gender.MALE)
                .setIdentityDocumentSeries("1234")
                .setIdentityDocumentNumber("567890")
                .setItn("770123456789")
                .setInsuranceNumber("12345678901")
                .setActualDate(actualDate)
                .setAddressDefined(true)
                .setResidenceAddressName("г. Москва, ул. Примерная, д. 1")
                .setStatus(ClientStatus.ACTUAL);
    }

    /**
     * Создаёт обновлённую версию {@link ClientTo}.
     *
     * @param source     исходное событие клиента
     * @param actualDate дата актуализации события
     * @return транспортный объект клиента
     */
    public static ClientTo updatedClientEvent(ClientTo source, OffsetDateTime actualDate) {
        return clientEvent(source.getId(), actualDate)
                .setLastName("Петров")
                .setFirstName("Пётр");
    }

    /**
     * Создаёт {@link ClientTo} для события удаления клиента.
     *
     * @param source     исходное событие клиента
     * @param actualDate дата актуализации события
     * @return транспортный объект клиента
     */
    public static ClientTo deletedClientEvent(ClientTo source, OffsetDateTime actualDate) {
        return clientEvent(source.getId(), actualDate)
                .setStatus(ClientStatus.DELETED);
    }
}
