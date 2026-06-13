package ru.kubsu.applicationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kubsu.applicationservice.entity.Client;
import ru.kubsu.applicationservice.mapper.ClientProjectionMapper;
import ru.kubsu.applicationservice.repository.ClientRepository;
import ru.kubsu.contracts.dto.service.client.ClientTo;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Сервис проекции клиента из событий client-service в локальную витрину.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientProjectionService {

    /** Репозиторий витрины клиентов. */
    private final ClientRepository clientRepository;

    /** Маппер проекции клиента. */
    private final ClientProjectionMapper clientProjectionMapper;

    /**
     * Применяет событие клиента к локальной витрине.
     * Устаревшие события (с меньшим {@code actualDate}) игнорируются.
     *
     * @param clientTo данные клиента из события
     */
    @Transactional
    public void applyClientEvent(ClientTo clientTo) {
        Optional<Client> existingClient = clientRepository.findById(clientTo.getId());
        if (existingClient.isPresent() && isStaleEvent(existingClient.get().getActualDate(), clientTo.getActualDate())) {
            log.debug("Пропуск устаревшего события клиента: id={}, incomingActualDate={}, storedActualDate={}",
                    clientTo.getId(), clientTo.getActualDate(), existingClient.get().getActualDate());
            return;
        }

        Client projectedClient = clientProjectionMapper.toEntity(clientTo);
        clientRepository.save(projectedClient);
    }

    /**
     * Проверяет, что входящее событие старее уже сохранённой проекции.
     *
     * @param storedActualDate   дата актуализации в витрине
     * @param incomingActualDate дата актуализации из события
     * @return {@code true}, если событие устарело
     */
    private boolean isStaleEvent(OffsetDateTime storedActualDate, OffsetDateTime incomingActualDate) {
        if (storedActualDate == null || incomingActualDate == null) {
            return false;
        }
        return incomingActualDate.isBefore(storedActualDate);
    }
}
