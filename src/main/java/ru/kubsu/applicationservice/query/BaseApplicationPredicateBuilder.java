package ru.kubsu.applicationservice.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.util.StringUtils;
import ru.kubsu.contracts.dto.service.application.BaseApplicationQueryParams;
import ru.kubsu.contracts.enums.service.application.ApplicationStatusBusiness;
import ru.kubsu.contracts.enums.service.client.Gender;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Общие QueryDSL-фильтры для поиска заявлений.
 */
public final class BaseApplicationPredicateBuilder {

    private BaseApplicationPredicateBuilder() {
    }

    /**
     * Добавляет базовые фильтры поиска заявлений.
     *
     * @param params  параметры поиска
     * @param builder построитель предиката
     * @param paths   пути полей базовой сущности
     */
    public static void apply(BaseApplicationQueryParams params,
                             BooleanBuilder builder,
                             BasePaths paths) {
        boolean deleted = params.getDeleted() != null ? params.getDeleted() : false;
        builder.and(paths.deleted().eq(deleted));

        if (params.getStatusBusiness() != null) {
            builder.and(paths.statusBusiness().eq(params.getStatusBusiness()));
        }
        if (StringUtils.hasText(params.getNumber())) {
            builder.and(paths.number().containsIgnoreCase(params.getNumber()));
        }
        if (params.getCreatedDateFrom() != null) {
            builder.and(paths.createdDate().goe(params.getCreatedDateFrom()));
        }
        if (params.getCreatedDateTo() != null) {
            builder.and(paths.createdDate().loe(params.getCreatedDateTo()));
        }
        if (StringUtils.hasText(params.getLastName())) {
            builder.and(paths.lastName().containsIgnoreCase(params.getLastName()));
        }
        if (StringUtils.hasText(params.getFirstName())) {
            builder.and(paths.firstName().containsIgnoreCase(params.getFirstName()));
        }
        if (StringUtils.hasText(params.getPatronymic())) {
            builder.and(paths.patronymic().containsIgnoreCase(params.getPatronymic()));
        }
        if (StringUtils.hasText(params.getIdentityDocumentSeries())) {
            builder.and(paths.identityDocumentSeries().eq(params.getIdentityDocumentSeries()));
        }
        if (StringUtils.hasText(params.getIdentityDocumentNumber())) {
            builder.and(paths.identityDocumentNumber().eq(params.getIdentityDocumentNumber()));
        }
        if (StringUtils.hasText(params.getItn())) {
            builder.and(paths.itn().eq(params.getItn()));
        }
        if (StringUtils.hasText(params.getInsuranceNumber())) {
            builder.and(paths.insuranceNumber().eq(params.getInsuranceNumber()));
        }
        if (params.getBirthDate() != null) {
            builder.and(paths.birthDate().eq(params.getBirthDate()));
        }
        if (params.getGender() != null) {
            builder.and(paths.gender().eq(params.getGender()));
        }
    }

    /**
     * Пути базовых полей заявления для QueryDSL-предиката.
     *
     * @param deleted                 признак удаления
     * @param statusBusiness          бизнес-статус
     * @param number                  номер
     * @param createdDate             дата создания
     * @param lastName                фамилия
     * @param firstName               имя
     * @param patronymic              отчество
     * @param identityDocumentSeries  серия документа
     * @param identityDocumentNumber  номер документа
     * @param itn                     ИНН
     * @param insuranceNumber         СНИЛС
     * @param birthDate               дата рождения
     * @param gender                  пол
     */
    public record BasePaths(
            BooleanPath deleted,
            EnumPath<ApplicationStatusBusiness> statusBusiness,
            StringPath number,
            DateTimePath<OffsetDateTime> createdDate,
            StringPath lastName,
            StringPath firstName,
            StringPath patronymic,
            StringPath identityDocumentSeries,
            StringPath identityDocumentNumber,
            StringPath itn,
            StringPath insuranceNumber,
            DatePath<LocalDate> birthDate,
            EnumPath<Gender> gender) {
    }
}
