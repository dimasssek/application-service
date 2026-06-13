--liquibase formatted sql

--changeset application-service:v14_create_table_general_check
create table if not exists general_check
(
    id               uuid primary key,
    application_type varchar                  not null,
    date_to          timestamp with time zone not null,
    processed_count  integer                  not null,
    in_queue_count   integer                  not null,
    bad_count        integer                  not null,
    applied_count    integer                  not null,
    rejected_count   integer                  not null,
    created_at       timestamp with time zone not null default now()
);

comment on table general_check is 'Результат пакетной обработки general-check';

comment on column general_check.id is 'Идентификатор запуска general-check';
comment on column general_check.application_type is 'Тип обрабатываемых заявлений';
comment on column general_check.date_to is 'Верхняя граница created_date заявлений в пакете';
comment on column general_check.processed_count is 'Количество обработанных заявлений';
comment on column general_check.in_queue_count is 'Количество заявлений в статусе IN_QUEUE после обработки';
comment on column general_check.bad_count is 'Количество заявлений в статусе BAD после обработки';
comment on column general_check.applied_count is 'Количество заявлений в статусе APPLIED после обработки';
comment on column general_check.rejected_count is 'Количество заявлений в статусе REJECTED после обработки';
comment on column general_check.created_at is 'Дата и время запуска general-check';
