--liquibase formatted sql

--changeset application-service:v12_alter_table_application_add_deleted
alter table application
    add column if not exists deleted boolean not null default false;

comment on column application.deleted is 'Признак мягкого удаления заявления';
