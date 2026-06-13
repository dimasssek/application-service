package ru.kubsu.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kubsu.applicationservice.entity.Client;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий витрины клиентов.
 */
public interface ClientRepository extends JpaRepository<Client, UUID> {

    /**
     * Ищет актуального клиента по ФИО и паспортным данным.
     *
     * @param lastName                 фамилия
     * @param firstName                имя
     * @param patronymic               отчество (оба null или равны)
     * @param identityDocumentSeries   серия документа
     * @param identityDocumentNumber   номер документа
     * @return найденный клиент
     */
    @Query("""
            SELECT c FROM Client c
            WHERE c.status = 'ACTUAL'
              AND c.lastName = :lastName
              AND c.firstName = :firstName
              AND ((:patronymic IS NULL AND c.patronymic IS NULL)
                   OR (:patronymic IS NOT NULL AND c.patronymic = :patronymic))
              AND c.identityDocumentSeries = :identityDocumentSeries
              AND c.identityDocumentNumber = :identityDocumentNumber
            """)
    Optional<Client> findMatchingActualClient(@Param("lastName") String lastName,
                                              @Param("firstName") String firstName,
                                              @Param("patronymic") String patronymic,
                                              @Param("identityDocumentSeries") String identityDocumentSeries,
                                              @Param("identityDocumentNumber") String identityDocumentNumber);
}
