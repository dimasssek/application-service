--liquibase formatted sql

--changeset application-service:v7_create_table_claim_application
create table if not exists claim_application
(
    application_id uuid primary key,
    claim_type     varchar not null,
    claim_subject  varchar not null,
    description    varchar,
    external_ref   varchar,

    constraint fk_claim_application_application
        foreign key (application_id)
            references application (id)
);

comment on table claim_application is 'Заявление-претензия или обращение';

comment on column claim_application.application_id is 'Идентификатор заявления';
comment on column claim_application.claim_type is 'Тип претензии или обращения';
comment on column claim_application.claim_subject is 'Предмет претензии или обращения';
comment on column claim_application.description is 'Описание претензии или обращения';
comment on column claim_application.external_ref is 'Внешняя ссылка на операцию, договор или иной объект';
