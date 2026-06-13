--liquibase formatted sql

--changeset application-service:v9_create_table_status_inform
create table if not exists status_inform
(
    id                             uuid primary key,
    code                           varchar not null,
    status_internal                varchar not null,
    product_status_business        varchar,
    service_status_business        varchar,
    profile_change_status_business varchar,
    document_status_business       varchar,
    claim_status_business          varchar,
    authority_status_business      varchar,
    reason                         varchar,

    constraint uq_status_inform_code unique (code),
    constraint uq_status_inform_status_internal unique (status_internal)
);

comment on table status_inform is 'Матрица соответствия внутренних и бизнес-статусов заявлений';

comment on column status_inform.id is 'Идентификатор';
comment on column status_inform.code is 'Код правила сопоставления статусов';
comment on column status_inform.status_internal is 'Внутренний статус заявления';
comment on column status_inform.product_status_business is 'Бизнес-статус для заявления на банковский продукт';
comment on column status_inform.service_status_business is 'Бизнес-статус для заявления на банковскую услугу';
comment on column status_inform.profile_change_status_business is 'Бизнес-статус для заявления на изменение данных клиента';
comment on column status_inform.document_status_business is 'Бизнес-статус для заявления на получение документа';
comment on column status_inform.claim_status_business is 'Бизнес-статус для заявления-претензии или обращения';
comment on column status_inform.authority_status_business is 'Бизнес-статус для заявления на полномочия и доступы';
comment on column status_inform.reason is 'Детализированный текст причины или пояснения для отображения на фронте';
