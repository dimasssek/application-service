package ru.kubsu.applicationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;

import java.util.UUID;

/**
 * Матрица соответствия внутренних и бизнес-статусов заявлений.
 */
@Entity
@Table(name = "status_inform")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class StatusInform {

    /** Идентификатор записи. */
    @Id
    private UUID id;

    /** Код правила сопоставления статусов. */
    @Column(name = "code", nullable = false)
    private String code;

    /** Внутренний статус заявления. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_internal", nullable = false)
    private ApplicationStatusInternal statusInternal;

    /** Бизнес-статус для заявления на банковский продукт. */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_status_business")
    private ApplicationStatusBusiness productStatusBusiness;

    /** Бизнес-статус для заявления на банковскую услугу. */
    @Enumerated(EnumType.STRING)
    @Column(name = "service_status_business")
    private ApplicationStatusBusiness serviceStatusBusiness;

    /** Бизнес-статус для заявления на изменение данных клиента. */
    @Enumerated(EnumType.STRING)
    @Column(name = "profile_change_status_business")
    private ApplicationStatusBusiness profileChangeStatusBusiness;

    /** Бизнес-статус для заявления на получение документа. */
    @Enumerated(EnumType.STRING)
    @Column(name = "document_status_business")
    private ApplicationStatusBusiness documentStatusBusiness;

    /** Бизнес-статус для заявления-претензии или обращения. */
    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status_business")
    private ApplicationStatusBusiness claimStatusBusiness;

    /** Бизнес-статус для заявления на полномочия и доступы. */
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_status_business")
    private ApplicationStatusBusiness authorityStatusBusiness;

    /** Текст причины для отображения на UI. */
    @Column(name = "reason")
    private String reason;
}
