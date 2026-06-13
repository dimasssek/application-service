--liquibase formatted sql

--changeset application-service:v3_create_table_product_application
create table if not exists product_application
(
    application_id      uuid primary key,
    product_category    varchar not null,
    product_code        varchar,
    product_name        varchar not null,
    amount              numeric,
    term_months         integer,
    purpose             varchar,
    tariff_code         varchar,
    external_product_id varchar,

    constraint fk_product_application_application
        foreign key (application_id)
            references application (id)
);

comment on table product_application is 'Заявление на банковский продукт';

comment on column product_application.application_id is 'Идентификатор заявления';
comment on column product_application.product_category is 'Категория банковского продукта';
comment on column product_application.product_code is 'Код банковского продукта';
comment on column product_application.product_name is 'Наименование банковского продукта';
comment on column product_application.amount is 'Сумма продукта';
comment on column product_application.term_months is 'Срок продукта в месяцах';
comment on column product_application.purpose is 'Цель оформления продукта';
comment on column product_application.tariff_code is 'Код тарифа';
comment on column product_application.external_product_id is 'Внешний идентификатор продукта';
