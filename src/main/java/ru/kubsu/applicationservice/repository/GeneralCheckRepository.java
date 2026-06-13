package ru.kubsu.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kubsu.applicationservice.entity.GeneralCheck;

import java.util.UUID;

/**
 * Репозиторий результатов general-check.
 */
public interface GeneralCheckRepository extends JpaRepository<GeneralCheck, UUID> {
}
