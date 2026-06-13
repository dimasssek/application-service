--liquibase formatted sql

--changeset application-service:v8_create_table_authority_application
create table if not exists authority_application
(
    application_id  uuid primary key,
    authority_type  varchar not null,
    person_name     varchar not null,
    document_number varchar,
    valid_until     date,

    constraint fk_authority_application_application
        foreign key (application_id)
            references application (id)
);

comment on table authority_application is 'Заявление на полномочия и доступы';

comment on column authority_application.application_id is 'Идентификатор заявления';
comment on column authority_application.authority_type is 'Тип полномочия или доступа';
comment on column authority_application.person_name is 'ФИО лица, которому предоставляются или изменяются полномочия';
comment on column authority_application.document_number is 'Номер документа, подтверждающего полномочия';
comment on column authority_application.valid_until is 'Дата окончания действия полномочий';
