package ru.kubsu.applicationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kubsu.applicationservice.entity.ApplicationEntity;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.applicationservice.repository.ClientRepository;

import java.util.Optional;

/**
 * Сервис поиска клиента в витрине по данным заявления.
 */
@Service
@RequiredArgsConstructor
public class ClientLookupService {

    /** Репозиторий витрины клиентов. */
    private final ClientRepository clientRepository;

    /**
     * Ищет актуального клиента по ФИО и паспортным данным заявления.
     *
     * @param application заявление
     * @return найденный клиент или пусто
     */
    public Optional<Client> findMatchingClient(ApplicationEntity application) {
        return clientRepository.findMatchingActualClient(
                application.getLastName(),
                application.getFirstName(),
                application.getPatronymic(),
                application.getIdentityDocumentSeries(),
                application.getIdentityDocumentNumber());
    }
}
