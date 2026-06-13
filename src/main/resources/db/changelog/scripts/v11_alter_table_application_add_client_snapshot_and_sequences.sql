--liquibase formatted sql

--changeset application-service:v11_alter_table_application_add_client_snapshot_and_sequences
alter table application
    alter column client_id drop not null;

alter table application
    add column last_name varchar not null,
    add column first_name varchar not null,
    add column patronymic varchar,
    add column birth_date date not null,
    add column gender varchar not null,
    add column identity_document_series varchar not null,
    add column identity_document_number varchar,
    add column itn varchar,
    add column insurance_number varchar;

comment on column application.last_name is 'Фамилия клиента на момент подачи заявления';
comment on column application.first_name is 'Имя клиента на момент подачи заявления';
comment on column application.patronymic is 'Отчество клиента на момент подачи заявления';
comment on column application.birth_date is 'Дата рождения клиента на момент подачи заявления';
comment on column application.gender is 'Пол клиента на момент подачи заявления';
comment on column application.identity_document_series is 'Серия документа клиента на момент подачи заявления';
comment on column application.identity_document_number is 'Номер документа клиента на момент подачи заявления';
comment on column application.itn is 'ИНН клиента на момент подачи заявления';
comment on column application.insurance_number is 'СНИЛС клиента на момент подачи заявления';

create sequence if not exists application_number_seq_product start with 1 increment by 1;
create sequence if not exists application_number_seq_service start with 1 increment by 1;
create sequence if not exists application_number_seq_profile_change start with 1 increment by 1;
create sequence if not exists application_number_seq_document start with 1 increment by 1;
create sequence if not exists application_number_seq_claim start with 1 increment by 1;
create sequence if not exists application_number_seq_authority start with 1 increment by 1;
