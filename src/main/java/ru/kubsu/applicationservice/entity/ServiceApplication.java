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
import ru.kubsu.contracts.enums.service.application.ServiceActionType;
import ru.kubsu.contracts.enums.service.application.ServiceType;

/**
 * Заявление на банковскую услугу.
 */
@Entity
@Table(name = "service_application")
@DiscriminatorValue("SERVICE")
@PrimaryKeyJoinColumn(name = "application_id")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class ServiceApplication extends ApplicationEntity {

    /** Тип банковской услуги. */
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    /** Наименование банковской услуги. */
    @Column(name = "service_name", nullable = false)
    private String serviceName;

    /** Тип действия по услуге. */
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ServiceActionType actionType;
}
