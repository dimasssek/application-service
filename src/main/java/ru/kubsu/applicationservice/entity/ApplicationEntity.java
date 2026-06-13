package ru.kubsu.applicationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusInternal;
import ru.kubsu.contracts.enums.service.application.ApplicationType;
import ru.kubsu.contracts.enums.service.application.Channel;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Базовая сущность заявления клиента (JOINED-наследование по типу).
 */
@Entity
@Table(name = "application")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "application_type")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public abstract class ApplicationEntity {

    /** Идентификатор заявления. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Идентификатор клиента (заполняется после post-processing). */
    @Column(name = "client_id")
    private UUID clientId;

    /** Номер заявления. */
    @Column(name = "number", nullable = false)
    private String number;

    /** Тип заявления (дискриминатор). */
    @Enumerated(EnumType.STRING)
    @Column(name = "application_type", insertable = false, updatable = false)
    private ApplicationType applicationType;

    /** Канал подачи заявления. */
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false)
    private Channel channel;

    /** Внутренний статус заявления. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_internal", nullable = false)
    private ApplicationStatusInternal statusInternal;

    /** Бизнес-статус заявления. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status_business", nullable = false)
    private ApplicationStatusBusiness statusBusiness;

    /** Дата создания заявления. */
    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;

    /** Дата закрытия заявления. */
    @Column(name = "closed_date")
    private OffsetDateTime closedDate;

    /** Признак мягкого удаления. */
    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    /** Фамилия клиента на момент подачи заявления. */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /** Имя клиента на момент подачи заявления. */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /** Отчество клиента на момент подачи заявления. */
    @Column(name = "patronymic")
    private String patronymic;

    /** Дата рождения клиента на момент подачи заявления. */
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /** Пол клиента на момент подачи заявления. */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    /** Серия документа клиента на момент подачи заявления. */
    @Column(name = "identity_document_series", nullable = false)
    private String identityDocumentSeries;

    /** Номер документа клиента на момент подачи заявления. */
    @Column(name = "identity_document_number")
    private String identityDocumentNumber;

    /** ИНН клиента на момент подачи заявления. */
    @Column(name = "itn")
    private String itn;

    /** СНИЛС клиента на момент подачи заявления. */
    @Column(name = "insurance_number")
    private String insuranceNumber;
}
