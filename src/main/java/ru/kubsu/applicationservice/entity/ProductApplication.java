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
import ru.kubsu.contracts.enums.service.application.ProductCategory;

import java.math.BigDecimal;

/**
 * Заявление на банковский продукт.
 */
@Entity
@Table(name = "product_application")
@DiscriminatorValue("PRODUCT")
@PrimaryKeyJoinColumn(name = "application_id")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@FieldNameConstants
public class ProductApplication extends ApplicationEntity {

    /** Категория банковского продукта. */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    private ProductCategory productCategory;

    /** Код банковского продукта. */
    @Column(name = "product_code")
    private String productCode;

    /** Наименование банковского продукта. */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /** Сумма продукта. */
    @Column(name = "amount")
    private BigDecimal amount;

    /** Срок продукта в месяцах. */
    @Column(name = "term_months")
    private Integer termMonths;

    /** Цель оформления продукта. */
    @Column(name = "purpose")
    private String purpose;

    /** Код тарифа. */
    @Column(name = "tariff_code")
    private String tariffCode;

    /** Внешний идентификатор продукта. */
    @Column(name = "external_product_id")
    private String externalProductId;
}
