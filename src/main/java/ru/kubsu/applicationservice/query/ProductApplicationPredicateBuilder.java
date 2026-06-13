package ru.kubsu.applicationservice.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.QApplicationEntity;
import ru.kubsu.applicationservice.entity.QProductApplication;
import ru.kubsu.contracts.dto.service.application.ProductApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * QueryDSL-фильтры поиска продуктовых заявлений.
 */
public final class ProductApplicationPredicateBuilder {

    private ProductApplicationPredicateBuilder() {
    }

    /**
     * Формирует предикат поиска по параметрам запроса.
     *
     * @param params параметры поиска
     * @return QueryDSL-предикат
     */
    public static Predicate build(ProductApplicationQueryParams params) {
        QApplicationEntity root = QApplicationEntity.applicationEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(root.applicationType.eq(ApplicationType.PRODUCT));
        BaseApplicationPredicateBuilder.apply(params, builder, ApplicationQueryPaths.from(root));

        QProductApplication product = QProductApplication.productApplication;
        if (params.getProductCategory() != null) {
            builder.and(productExists(root, product, product.productCategory.eq(params.getProductCategory())));
        }
        if (StringUtils.hasText(params.getProductCode())) {
            builder.and(productExists(root, product, product.productCode.eq(params.getProductCode())));
        }
        if (StringUtils.hasText(params.getProductName())) {
            builder.and(productExists(root, product, product.productName.containsIgnoreCase(params.getProductName())));
        }
        return builder;
    }

    private static Predicate productExists(QApplicationEntity root,
                                           QProductApplication product,
                                           Predicate extra) {
        return JPAExpressions.selectOne()
                .from(product)
                .where(product.id.eq(root.id).and(extra))
                .exists();
    }
}
