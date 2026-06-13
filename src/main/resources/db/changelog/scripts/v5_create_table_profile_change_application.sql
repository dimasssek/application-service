--liquibase formatted sql

--changeset application-service:v5_create_table_profile_change_application
create table if not exists profile_change_application
(
    application_id uuid primary key,
    change_type    varchar not null,
    old_value      varchar,
    new_value      varchar,

    constraint fk_profile_change_application_application
        foreign key (application_id)
            references application (id)
);

comment on table profile_change_application is 'Заявление на изменение данных клиента';

comment on column profile_change_application.application_id is 'Идентификатор заявления';
comment on column profile_change_application.change_type is 'Тип изменяемых данных';
comment on column profile_change_application.old_value is 'Старое значение';
comment on column profile_change_application.new_value is 'Новое значение';
