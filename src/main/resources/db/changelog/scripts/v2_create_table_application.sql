--liquibase formatted sql

--changeset application-service:v2_create_table_application
create table if not exists application
(
    id               uuid primary key,
    client_id        uuid not null,
    number           varchar not null,
    application_type varchar not null,
    channel          varchar not null,
    status_internal  varchar not null,
    status_business  varchar not null,
    created_date     timestamp with time zone not null,
    closed_date      timestamp with time zone,

    constraint fk_application_client
        foreign key (client_id)
            references client (id)
);

comment on table application is 'Заявление клиента';

comment on column application.id is 'Идентификатор заявления';
comment on column application.client_id is 'Идентификатор клиента';
comment on column application.number is 'Номер заявления';
comment on column application.application_type is 'Тип заявления';
comment on column application.channel is 'Канал подачи заявления';
comment on column application.status_internal is 'Внутренний статус заявления с детализацией обработки';
comment on column application.status_business is 'Бизнес-статус заявления';
comment on column application.created_date is 'Дата создания заявления';
comment on column application.closed_date is 'Дата закрытия заявления';
