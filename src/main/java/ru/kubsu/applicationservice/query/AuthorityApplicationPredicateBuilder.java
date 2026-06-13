package ru.kubsu.applicationservice.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.QApplicationEntity;
import ru.kubsu.applicationservice.entity.QAuthorityApplication;
import ru.kubsu.contracts.dto.service.application.AuthorityApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * QueryDSL-фильтры поиска заявлений на полномочия.
 */
public final class AuthorityApplicationPredicateBuilder {

    private AuthorityApplicationPredicateBuilder() {
    }

    /**
     * Формирует предикат поиска по параметрам запроса.
     *
     * @param params параметры поиска
     * @return QueryDSL-предикат
     */
    public static Predicate build(AuthorityApplicationQueryParams params) {
        QApplicationEntity root = QApplicationEntity.applicationEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(root.applicationType.eq(ApplicationType.AUTHORITY));
        BaseApplicationPredicateBuilder.apply(params, builder, ApplicationQueryPaths.from(root));

        QAuthorityApplication authority = QAuthorityApplication.authorityApplication;
        if (params.getAuthorityType() != null) {
            builder.and(JPAExpressions.selectOne()
                    .from(authority)
                    .where(authority.id.eq(root.id)
                            .and(authority.authorityType.eq(params.getAuthorityType())))
                    .exists());
        }
        if (StringUtils.hasText(params.getPersonName())) {
            builder.and(JPAExpressions.selectOne()
                    .from(authority)
                    .where(authority.id.eq(root.id)
                            .and(authority.personName.containsIgnoreCase(params.getPersonName())))
                    .exists());
        }
        return builder;
    }
}
