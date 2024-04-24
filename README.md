![Project version](https://img.shields.io/badge/Project_version-1.0.0-gree)
![Contributors](https://img.shields.io/badge/Contributors-1-blue)

Election Traffic: Polling Station Load Service API
================================================

Данная система является API-сервером для 
[клиентского приложения](https://github.com/watisluv1910/csjs2024-project_client) 
**Election Traffic: Polling Station Load Service**, разработаной как часть итогового
практического задания для CROC Spring Java School 2024.

## Краткое описание проекта

Основная цель данного проекта - сбор статистики об избирательных участках и их
посещаемости во время проведения электоральных процедур. После
использования приложения и получения предоставляемых им данных,
пользователь сможет как составить комплексную оценку посещаемости выборов,
так и просто решить, в какое время ему завтра лучше идти голосовать,
чтобы провести как можно меньшее количество времени в очереди перед участком.

> Для обеспечения целостности данных и определённой доли исторической достоверности,
в качестве временного промежутка, на котором проходит
электоральная процедура, выбран период между **15.03.2024** и **17.03.2024**.

## Стэк используемых технологий

- Spring (Java)
- PostgresSQL
- Flyway
- **OpenAPI V3** - спецификация для описания API
- Docker
- **Git** - система контроля версий

## Работа с проектом

### Методы запуска сервиса и необходимое программное обеспечение

Запуск сервиса возможен тремя способами:
- Частично локально - необходима [Java](https://www.oracle.com/java/technologies/downloads/) >= v21 и [Docker](https://docs.docker.com/install/).
- Полностью в изолированной среде Docker - необходим [Docker](https://docs.docker.com/install/) c [Docker Compose](https://docs.docker.com/compose/install/).

Далее вышеперечисленные методы запуска приложения будут описаны подробно.

#### Частично локальный запуск

1. Клонировать репозиторий с проектом:
   ```shell
   git clone git@github.com:watisluv1910/csjs2024-project_test-api.git
   ```
2. Если необходимо, установить ПО, перечисленное в соотвествующем [разделе](#методы-запуска-сервиса-и-необходимое-программное-обеспечение).
3. Предварительно запустив Docker, запустить базу данных как Docker контейнер с помощью команды:
   ```shell
   docker run -d --name polling_stations_api_db --env 'POSTGRES_USER=postgres' \
    --env 'POSTGRES_PASSWORD=postgres' --env 'POSTGRES_DB=polling_stations_db' \
    --env 'PG_TRUST_LOCALNET=true' -p 5433:5432 postgres:latest
   ```
4. Для запуска сервиса необходимо выполнить следующую команду в [**корневой директории**](/) проекта
   <br>Для Unix-подобных систем:
   ```shellPersistence
   ./mvnw spring-boot:run
   ``` 
   Для Windows:
    ```cmd
    mvnw.cmd spring-boot:run
    ```

#### Запуск в изолированной среде Docker 

1. Запустить [Docker](https://docs.docker.com/install/) с [Docker Compose](https://docs.docker.com/compose/install/).
2. В [**корневой директории**](/) проекта выполнить команду `docker-compose up -d`.
3. После использования сервиса и тестирования API ввести команду `docker-compose down`.
