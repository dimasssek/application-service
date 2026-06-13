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
import ru.kubsu.contracts.enums.service.application.ClaimType;

/**
 * Заявление-претензия или обращение клиента.
 */
@Entity
@Table(name = "claim_application")
@DiscriminatorValue("CLAIM")
@PrimaryKeyJoinColumn(name = "application_id")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class ClaimApplication extends ApplicationEntity {

    /** Тип претензии или обращения. */
    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    private ClaimType claimType;

    /** Предмет претензии или обращения. */
    @Column(name = "claim_subject", nullable = false)
    private String claimSubject;

    /** Описание претензии или обращения. */
    @Column(name = "description")
    private String description;

    /** Внешняя ссылка на операцию, договор или иной объект. */
    @Column(name = "external_ref")
    private String externalRef;
}
