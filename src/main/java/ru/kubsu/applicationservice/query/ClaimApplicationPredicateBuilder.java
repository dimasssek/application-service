package ru.kubsu.applicationservice.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import org.springframework.util.StringUtils;
import ru.kubsu.applicationservice.entity.QApplicationEntity;
import ru.kubsu.applicationservice.entity.QClaimApplication;
import ru.kubsu.contracts.dto.service.application.ClaimApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationType;

/**
 * QueryDSL-фильтры поиска заявлений-претензий.
 */
public final class ClaimApplicationPredicateBuilder {

    private ClaimApplicationPredicateBuilder() {
    }

    /**
     * Формирует предикат поиска по параметрам запроса.
     *
     * @param params параметры поиска
     * @return QueryDSL-предикат
     */
    public static Predicate build(ClaimApplicationQueryParams params) {
        QApplicationEntity root = QApplicationEntity.applicationEntity;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(root.applicationType.eq(ApplicationType.CLAIM));
        BaseApplicationPredicateBuilder.apply(params, builder, ApplicationQueryPaths.from(root));

        QClaimApplication claim = QClaimApplication.claimApplication;
        if (params.getClaimType() != null) {
            builder.and(JPAExpressions.selectOne()
                    .from(claim)
                    .where(claim.id.eq(root.id).and(claim.claimType.eq(params.getClaimType())))
                    .exists());
        }
        if (StringUtils.hasText(params.getClaimSubject())) {
            builder.and(JPAExpressions.selectOne()
                    .from(claim)
                    .where(claim.id.eq(root.id)
                            .and(claim.claimSubject.containsIgnoreCase(params.getClaimSubject())))
                    .exists());
        }
        return builder;
    }
}
