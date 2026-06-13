package ru.kubsu.applicationservice.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.QApplicationEntity;
import ru.kubsu.applicationservice.entity.QServiceApplication;
import ru.kubsu.contracts.dto.service.application.ServiceApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * QueryDSL-фильтры поиска заявлений на банковские услуги.
 */
public final class ServiceApplicationPredicateBuilder {

    private ServiceApplicationPredicateBuilder() {
    }

    /**
     * Формирует предикат поиска по параметрам запроса.
     *
     * @param params параметры поиска
     * @return QueryDSL-предикат
     */
    public static Predicate build(ServiceApplicationQueryParams params) {
        QApplicationEntity root = QApplicationEntity.applicationEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(root.applicationType.eq(ApplicationType.SERVICE));
        BaseApplicationPredicateBuilder.apply(params, builder, ApplicationQueryPaths.from(root));

        QServiceApplication service = QServiceApplication.serviceApplication;
        if (params.getServiceType() != null) {
            builder.and(subtypeExists(root, service, service.serviceType.eq(params.getServiceType())));
        }
        if (StringUtils.hasText(params.getServiceName())) {
            builder.and(subtypeExists(root, service,
                    service.serviceName.containsIgnoreCase(params.getServiceName())));
        }
        if (params.getActionType() != null) {
            builder.and(subtypeExists(root, service, service.actionType.eq(params.getActionType())));
        }
        return builder;
    }

    private static Predicate subtypeExists(QApplicationEntity root,
                                           QServiceApplication service,
                                           com.querydsl.core.types.Predicate extra) {
        return JPAExpressions.selectOne()
                .from(service)
                .where(service.id.eq(root.id).and(extra))
                .exists();
    }
}
