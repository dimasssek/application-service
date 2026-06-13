# application-service

Микросервис обработки банковских заявлений (ядро BSPS).

## Локальный запуск инфраструктуры

> **Используйте [bsps-infra](https://github.com/dimasssek/bsps-infra)** — единый `docker compose` для Postgres, RabbitMQ, Grafana/Loki и др.
> Локальный `docker-compose.yml` в этом репозитории **deprecated**.

```bash
cd ../bsps-infra
docker compose up -d
```

RabbitMQ — общий брокер (порт 5672, UI 15672). Подробнее: `bsps-infra/docs/LOCAL_SETUP.md`.

Параметры подключения к БД и брокеру — в `src/main/resources/application.yml`.

## Сборка

```bash
./mvnw clean compile
```

## Контракты (submodule)

```bash
git submodule update --init --recursive
```

Общие DTO, enum-ы и messaging-модели — репозиторий [contracts](https://github.com/dimasssek/contracts.git), директория `src/contracts`.

## Swagger UI

После запуска сервиса: http://localhost:8082/swagger-ui.html
