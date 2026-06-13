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
import ru.kubsu.contracts.enums.service.application.ClientChangeType;

/**
 * Заявление на изменение данных клиента.
 */
@Entity
@Table(name = "profile_change_application")
@DiscriminatorValue("PROFILE_CHANGE")
@PrimaryKeyJoinColumn(name = "application_id")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class ProfileChangeApplication extends ApplicationEntity {

    /** Тип изменяемых данных. */
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private ClientChangeType changeType;

    /** Старое значение. */
    @Column(name = "old_value")
    private String oldValue;

    /** Новое значение. */
    @Column(name = "new_value")
    private String newValue;
}
