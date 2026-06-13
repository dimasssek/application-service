package ru.kubsu.applicationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kubsu.applicationservice.entity.ServiceApplication;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Репозиторий заявлений на банковские услуги.
 */
public interface ServiceApplicationRepository extends JpaRepository<ServiceApplication, UUID> {

    /**
     * Возвращает заявления для пакетной обработки general-check.
     *
     * @param dateTo верхняя граница created_date (включительно)
     * @return список заявлений
     */
    @Query("""
            SELECT a FROM ServiceApplication a
            WHERE a.deleted = false
              AND a.statusBusiness = 'IN_QUEUE'
              AND a.createdDate <= :dateTo
            ORDER BY a.createdDate ASC
            """)
    List<ServiceApplication> findForGeneralCheck(@Param("dateTo") OffsetDateTime dateTo);
}
