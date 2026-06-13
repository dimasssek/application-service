--liquibase formatted sql

--changeset application-service:v4_create_table_service_application
create table if not exists service_application
(
    application_id uuid primary key,
    service_type   varchar not null,
    service_name   varchar not null,
    action_type    varchar not null,

    constraint fk_service_application_application
        foreign key (application_id)
            references application (id)
);

comment on table service_application is 'Заявление на банковскую услугу';

comment on column service_application.application_id is 'Идентификатор заявления';
comment on column service_application.service_type is 'Тип банковской услуги';
comment on column service_application.service_name is 'Наименование банковской услуги';
comment on column service_application.action_type is 'Тип действия по услуге';
