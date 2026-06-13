package ru.kubsu.applicationservice.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import ru.kubsu.applicationservice.entity.QApplicationEntity;
import ru.kubsu.applicationservice.entity.QProfileChangeApplication;
import ru.kubsu.contracts.dto.service.application.ProfileChangeApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * QueryDSL-фильтры поиска заявлений на изменение данных клиента.
 */
public final class ProfileChangeApplicationPredicateBuilder {

    private ProfileChangeApplicationPredicateBuilder() {
    }

    /**
     * Формирует предикат поиска по параметрам запроса.
     *
     * @param params параметры поиска
     * @return QueryDSL-предикат
     */
    public static Predicate build(ProfileChangeApplicationQueryParams params) {
        QApplicationEntity root = QApplicationEntity.applicationEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(root.applicationType.eq(ApplicationType.PROFILE_CHANGE));
        BaseApplicationPredicateBuilder.apply(params, builder, ApplicationQueryPaths.from(root));

        if (params.getChangeType() != null) {
            QProfileChangeApplication profileChange = QProfileChangeApplication.profileChangeApplication;
            builder.and(JPAExpressions.selectOne()
                    .from(profileChange)
                    .where(profileChange.id.eq(root.id)
                            .and(profileChange.changeType.eq(params.getChangeType())))
                    .exists());
        }
        return builder;
    }
}
