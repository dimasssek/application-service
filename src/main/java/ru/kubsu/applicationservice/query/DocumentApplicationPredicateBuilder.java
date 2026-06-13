package ru.kubsu.applicationservice.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import ru.kubsu.applicationservice.entity.QApplicationEntity;
import ru.kubsu.applicationservice.entity.QDocumentApplication;
import ru.kubsu.contracts.dto.service.application.DocumentApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * QueryDSL-фильтры поиска заявлений на получение документа.
 */
public final class DocumentApplicationPredicateBuilder {

    private DocumentApplicationPredicateBuilder() {
    }

    /**
     * Формирует предикат поиска по параметрам запроса.
     *
     * @param params параметры поиска
     * @return QueryDSL-предикат
     */
    public static Predicate build(DocumentApplicationQueryParams params) {
        QApplicationEntity root = QApplicationEntity.applicationEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(root.applicationType.eq(ApplicationType.DOCUMENT));
        BaseApplicationPredicateBuilder.apply(params, builder, ApplicationQueryPaths.from(root));

        QDocumentApplication document = QDocumentApplication.documentApplication;
        if (params.getDocumentType() != null) {
            builder.and(JPAExpressions.selectOne()
                    .from(document)
                    .where(document.id.eq(root.id)
                            .and(document.documentType.eq(params.getDocumentType())))
                    .exists());
        }
        if (params.getDeliveryType() != null) {
            builder.and(JPAExpressions.selectOne()
                    .from(document)
                    .where(document.id.eq(root.id)
                            .and(document.deliveryType.eq(params.getDeliveryType())))
                    .exists());
        }
        return builder;
    }
}
