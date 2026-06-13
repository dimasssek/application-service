--liquibase formatted sql

--changeset application-service:v6_create_table_document_application
create table if not exists document_application
(
    application_id uuid primary key,
    document_type  varchar not null,
    delivery_type  varchar,
    purpose        varchar,

    constraint fk_document_application_application
        foreign key (application_id)
            references application (id)
);

comment on table document_application is 'Заявление на получение документа';

comment on column document_application.application_id is 'Идентификатор заявления';
comment on column document_application.document_type is 'Тип запрашиваемого документа';
comment on column document_application.delivery_type is 'Способ получения документа';
comment on column document_application.purpose is 'Цель получения документа';
