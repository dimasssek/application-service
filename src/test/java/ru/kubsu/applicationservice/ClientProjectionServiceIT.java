package ru.kubsu.applicationservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.applicationservice.repository.ClientRepository;
import ru.kubsu.applicationservice.service.ClientProjectionService;
import ru.kubsu.applicationservice.support.ClientTestFixtures;
import ru.kubsu.applicationservice.support.IntegrationTestBase;
import ru.kubsu.contracts.dto.service.client.ClientTo;
import ru.kubsu.contracts.enums.service.client.ClientStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет проекцию клиента из событий client-service.
 */
class ClientProjectionServiceIT extends IntegrationTestBase {

    @Autowired
    private ClientProjectionService clientProjectionService;

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Новое событие должно сохранять клиента в витрине.
     */
    @Test
    void shouldSaveClientWhenEventIsNew() {
        OffsetDateTime actualDate = OffsetDateTime.parse("2026-05-31T10:00:00Z");
        ClientTo event = ClientTestFixtures.clientEvent(UUID.randomUUID(), actualDate);

        clientProjectionService.applyClientEvent(event);

        Client savedClient = clientRepository.findById(event.getId()).orElseThrow();
        assertThat(savedClient.getLastName()).isEqualTo("Иванов");
        assertThat(savedClient.getActualDate()).isEqualTo(actualDate);
        assertThat(savedClient.getStatus()).isEqualTo(ClientStatus.ACTUAL);
    }

    /**
     * Более новое событие должно обновлять витрину.
     */
    @Test
    void shouldUpdateClientWhenEventIsNewer() {
        UUID clientId = UUID.randomUUID();
        OffsetDateTime olderDate = OffsetDateTime.parse("2026-05-31T10:00:00Z");
        OffsetDateTime newerDate = OffsetDateTime.parse("2026-05-31T11:00:00Z");
        ClientTo createdEvent = ClientTestFixtures.clientEvent(clientId, olderDate);
        clientProjectionService.applyClientEvent(createdEvent);

        clientProjectionService.applyClientEvent(ClientTestFixtures.updatedClientEvent(createdEvent, newerDate));

        Client savedClient = clientRepository.findById(clientId).orElseThrow();
        assertThat(savedClient.getLastName()).isEqualTo("Петров");
        assertThat(savedClient.getActualDate()).isEqualTo(newerDate);
    }

    /**
     * Устаревшее событие не должно перезаписывать более новую проекцию.
     */
    @Test
    void shouldIgnoreEventWhenEventIsStale() {
        UUID clientId = UUID.randomUUID();
        OffsetDateTime newerDate = OffsetDateTime.parse("2026-05-31T11:00:00Z");
        OffsetDateTime olderDate = OffsetDateTime.parse("2026-05-31T10:00:00Z");
        ClientTo updatedEvent = ClientTestFixtures.updatedClientEvent(
                ClientTestFixtures.clientEvent(clientId, olderDate), newerDate);
        clientProjectionService.applyClientEvent(updatedEvent);

        clientProjectionService.applyClientEvent(ClientTestFixtures.clientEvent(clientId, olderDate));

        Client savedClient = clientRepository.findById(clientId).orElseThrow();
        assertThat(savedClient.getLastName()).isEqualTo("Петров");
        assertThat(savedClient.getActualDate()).isEqualTo(newerDate);
    }

    /**
     * Событие удаления должно помечать клиента как DELETED.
     */
    @Test
    void shouldMarkClientDeletedWhenDeletedEventReceived() {
        UUID clientId = UUID.randomUUID();
        OffsetDateTime createdDate = OffsetDateTime.parse("2026-05-31T10:00:00Z");
        OffsetDateTime deletedDate = OffsetDateTime.parse("2026-05-31T12:00:00Z");
        ClientTo createdEvent = ClientTestFixtures.clientEvent(clientId, createdDate);
        clientProjectionService.applyClientEvent(createdEvent);

        clientProjectionService.applyClientEvent(ClientTestFixtures.deletedClientEvent(createdEvent, deletedDate));

        Client savedClient = clientRepository.findById(clientId).orElseThrow();
        assertThat(savedClient.getStatus()).isEqualTo(ClientStatus.DELETED);
        assertThat(savedClient.getActualDate()).isEqualTo(deletedDate);
    }
}
