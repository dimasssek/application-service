package ru.kubsu.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kubsu.applicationservice.entity.ClaimApplication;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий заявлений на претензии.
 */
public interface ClaimApplicationRepository extends JpaRepository<ClaimApplication, UUID> {

    /**
     * Возвращает заявления для пакетной обработки general-check.
     *
     * @param dateTo верхняя граница created_date (включительно)
     * @return список заявлений
     */
    @Query("""
            SELECT a FROM ClaimApplication a
            WHERE a.deleted = false
              AND a.statusBusiness = 'IN_QUEUE'
              AND a.createdDate <= :dateTo
            ORDER BY a.createdDate ASC
            """)
    List<ClaimApplication> findForGeneralCheck(@Param("dateTo") OffsetDateTime dateTo);
}
