--liquibase formatted sql

--changeset application-service:v10_refill_status_inform runOnChange:true splitStatements:false stripComments:false

create or replace procedure refill_status_inform()
    language plpgsql
    security definer
as
$$
begin
    insert into status_inform (
        id,
        code,
        status_internal,
        product_status_business,
        service_status_business,
        profile_change_status_business,
        document_status_business,
        claim_status_business,
        authority_status_business,
        reason
    ) values
    ('a1b2c3d4-e5f6-7890-abcd-ef1111110101', '101', 'APPLIED',
     'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED',
     'Заявление успешно обработано'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110102', '102', 'DUPLICATE',
     'BAD', 'BAD', 'BAD', 'BAD', 'BAD', 'BAD',
     'Найдено повторное заявление'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110104', '104', 'INTERNAL_ERROR',
     'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED',
     'Внутренняя ошибка при обработке'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110105', '105', 'IN_QUEUE',
     'IN_QUEUE', 'IN_QUEUE', 'IN_QUEUE', 'IN_QUEUE', 'IN_QUEUE', 'IN_QUEUE',
     'Ожидает обработки'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110201', '201', 'FLC_EMPTY_REQUIRED',
     'BAD', 'BAD', 'BAD', 'BAD', 'BAD', 'BAD',
     'Не заполнены обязательные поля'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110202', '202', 'FLC_INVALID_SUBMISSION',
     'BAD', 'BAD', 'BAD', 'BAD', 'BAD', 'BAD',
     'Дата подачи заявления больше текущей даты'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110301', '301', 'CLIENT_NOT_FOUND',
     'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED',
     'Клиент не найден'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110302', '302', 'ITN_NOT_ACTUAL',
     'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED',
     'ИНН в заявлении не соответствует данным клиента'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110303', '303', 'INSURANCE_NUMBER_NOT_ACTUAL',
     'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED', 'APPLIED',
     'СНИЛС в заявлении не соответствует данным клиента'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110304', '304', 'ITN_NOT_REQUIRED',
     'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED',
     'Не указан ИНН'),

    ('a1b2c3d4-e5f6-7890-abcd-ef1111110305', '305', 'INSURANCE_NUMBER_NOT_REQUIRED',
     'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED', 'REJECTED',
     'Не указан СНИЛС')

    on conflict (id) do update set
        code                                    = excluded.code,
        status_internal                         = excluded.status_internal,
        product_status_business                 = excluded.product_status_business,
        service_status_business                 = excluded.service_status_business,
        profile_change_status_business          = excluded.profile_change_status_business,
        document_status_business                = excluded.document_status_business,
        claim_status_business                   = excluded.claim_status_business,
        authority_status_business               = excluded.authority_status_business,
        reason                                  = excluded.reason;
end;
$$;

call refill_status_inform();
