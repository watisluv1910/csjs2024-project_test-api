The Polling Stations Attendance Service API
================================================
Test API for Polling stations Attendance Service.

Requirements
------------
- [Docker](https://docs.docker.com/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)

Development
-----------
1. Run Postgres database as Docker container:
    - Create and start container: `docker run -d --name polling_stations_api_db --env 'POSTGRES_USER=postgres' --env 'POSTGRES_PASSWORD=postgres' --env 'POSTGRES_DB=polling_stations_db' --env 'PG_TRUST_LOCALNET=true' -p 5433:5432 postgres:latest`.
    - Stop running container: `docker stop polling_stations_api_db`.
    - Start container: `docker start polling_stations_api_db`.
    - Remove container: `docker rm polling_stations_api_db`.
2. Go to location of `pom.xml` and run Spring Boot application: `mvn spring-boot:run`.
3. Test API.

Docker Compose usage
--------------------
1. Run Docker-Compose command to create and start application and database: `docker-compose up -d`.
2. When application starts, test API.
3. Run Docker-Compose command to stop and destroy application and database: `docker-compose down`.

##### Docker Compose commands:
- `docker-compose up` - create and start Docker containers
- `docker-compose up -d` - create and start Docker containers in the background
- `docker-compose down` - stop and destroy Docker containers
- `docker-compose start` - start Docker containers
- `docker-compose stop` - stop Docker containers
- `docker-compose logs -f` - tailing logs of Docker containers
- `docker-compose ps` - check status of Docker containers

##### Persistence
For PostgreSQL to preserve its state across container destroy and create, 
Docker mount a volume located in `docker/storage` (defined in a Dockerfile).
