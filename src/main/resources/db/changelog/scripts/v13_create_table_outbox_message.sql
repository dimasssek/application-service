--liquibase formatted sql

--changeset application-service:v13_create_table_outbox_message
create table if not exists outbox_message
(
    id              uuid primary key,
    aggregate_type  varchar                  not null,
    aggregate_id    uuid                     not null,
    event_type      varchar                  not null,
    payload         jsonb                    not null,
    exchange_name   varchar                  not null,
    routing_key     varchar                  not null,
    status          varchar                  not null,
    attempts        integer                  not null default 0,
    last_error      text,
    created_at      timestamp with time zone not null default now(),
    sent_at         timestamp with time zone
);

create index if not exists ix_outbox_message_status_created on outbox_message (status, created_at);

comment on table outbox_message is 'Outbox-сообщения для надёжной публикации в RabbitMQ';

comment on column outbox_message.id is 'Идентификатор outbox-записи';
comment on column outbox_message.aggregate_type is 'Тип агрегата (APPLICATION, PRODUCT_APPLICATION и т.д.)';
comment on column outbox_message.aggregate_id is 'Идентификатор агрегата';
comment on column outbox_message.event_type is 'Тип события';
comment on column outbox_message.payload is 'Тело сообщения в формате JSON';
comment on column outbox_message.exchange_name is 'Имя exchange для публикации';
comment on column outbox_message.routing_key is 'Routing key для публикации';
comment on column outbox_message.status is 'Статус обработки outbox-записи';
comment on column outbox_message.attempts is 'Количество попыток отправки';
comment on column outbox_message.last_error is 'Текст последней ошибки отправки';
comment on column outbox_message.created_at is 'Дата создания outbox-записи';
comment on column outbox_message.sent_at is 'Дата успешной отправки';
