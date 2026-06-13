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
import ru.kubsu.contracts.enums.service.application.DocumentDeliveryType;
import ru.kubsu.contracts.enums.service.application.DocumentType;

/**
 * Заявление на получение документа.
 */
@Entity
@Table(name = "document_application")
@DiscriminatorValue("DOCUMENT")
@PrimaryKeyJoinColumn(name = "application_id")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class DocumentApplication extends ApplicationEntity {

    /** Тип запрашиваемого документа. */
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    /** Способ получения документа. */
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DocumentDeliveryType deliveryType;

    /** Цель получения документа. */
    @Column(name = "purpose")
    private String purpose;
}
