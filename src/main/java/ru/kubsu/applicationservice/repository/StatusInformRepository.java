package ru.kubsu.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kubsu.applicationservice.entity.StatusInform;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий матрицы соответствия статусов заявлений.
 */
public interface StatusInformRepository extends JpaRepository<StatusInform, UUID> {

    /**
     * Находит запись матрицы по внутреннему статусу.
     *
     * @param statusInternal внутренний статус
     * @return запись status_inform
     */
    Optional<StatusInform> findByStatusInternal(ApplicationStatusInternal statusInternal);

    /**
     * Находит запись матрицы по коду правила.
     *
     * @param code код правила
     * @return запись status_inform
     */
    Optional<StatusInform> findByCode(String code);
}
