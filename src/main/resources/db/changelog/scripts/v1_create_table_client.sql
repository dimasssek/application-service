--liquibase formatted sql

--changeset application-service:v1_create_table_client
create table if not exists client
(
    id                           uuid primary key,
    last_name                    varchar not null,
    first_name                   varchar not null,
    patronymic                   varchar,
    birth_date                   date    not null,
    gender                       varchar not null,
    identity_document_series     varchar not null,
    identity_document_number     varchar,
    identity_document_issue_date date,
    itn                          varchar,
    insurance_number             varchar,
    actual_date                  timestamp with time zone,
    is_address_defined           boolean not null default false,
    residence_address_name       varchar,
    status                       varchar
);

comment on table client is 'Витрина клиентов для системы обработки заявлений';

comment on column client.id is 'Идентификатор клиента';
comment on column client.last_name is 'Фамилия';
comment on column client.first_name is 'Имя';
comment on column client.patronymic is 'Отчество';
comment on column client.birth_date is 'Дата рождения';
comment on column client.gender is 'Пол';
comment on column client.identity_document_series is 'Серия паспорта';
comment on column client.identity_document_number is 'Номер паспорта';
comment on column client.identity_document_issue_date is 'Дата выдачи паспорта';
comment on column client.itn is 'ИНН';
comment on column client.insurance_number is 'СНИЛС';
comment on column client.actual_date is 'Дата актуализации записи';
comment on column client.is_address_defined is 'Признак определённости адреса';
comment on column client.residence_address_name is 'Адрес места жительства';
comment on column client.status is 'Статус клиента';
