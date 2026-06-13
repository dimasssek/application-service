package ru.kubsu.applicationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Результат запуска general-check.
 */
@Entity
@Table(name = "general_check")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class GeneralCheck {

    /** Идентификатор запуска. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Тип обработанных заявлений. */
    @Enumerated(EnumType.STRING)
    @Column(name = "application_type", nullable = false)
    private ApplicationType applicationType;

    /** Верхняя граница created_date заявлений в пакете. */
    @Column(name = "date_to", nullable = false)
    private OffsetDateTime dateTo;

    /** Количество обработанных заявлений. */
    @Column(name = "processed_count", nullable = false)
    private int processedCount;

    /** Количество заявлений в статусе IN_QUEUE. */
    @Column(name = "in_queue_count", nullable = false)
    private int inQueueCount;

    /** Количество заявлений в статусе BAD. */
    @Column(name = "bad_count", nullable = false)
    private int badCount;

    /** Количество заявлений в статусе APPLIED. */
    @Column(name = "applied_count", nullable = false)
    private int appliedCount;

    /** Количество заявлений в статусе REJECTED. */
    @Column(name = "rejected_count", nullable = false)
    private int rejectedCount;

    /** Дата и время запуска. */
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
