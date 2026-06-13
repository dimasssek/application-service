package ru.kubsu.applicationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import ru.kubsu.contracts.enums.service.application.AuthorityType;

import java.time.LocalDate;

/**
 * Заявление на полномочия и доступы.
 */
@Entity
@Table(name = "authority_application")
@DiscriminatorValue("AUTHORITY")
@PrimaryKeyJoinColumn(name = "application_id")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class AuthorityApplication extends ApplicationEntity {

    /** Тип полномочия или доступа. */
    @Enumerated(EnumType.STRING)
    @Column(name = "authority_type", nullable = false)
    private AuthorityType authorityType;

    /** ФИО лица, которому предоставляются полномочия. */
    @Column(name = "person_name", nullable = false)
    private String personName;

    /** Номер документа, подтверждающего полномочия. */
    @Column(name = "document_number")
    private String documentNumber;

    /** Дата окончания действия полномочий. */
    @Column(name = "valid_until")
    private LocalDate validUntil;
}
