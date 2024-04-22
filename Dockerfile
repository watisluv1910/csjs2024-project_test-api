FROM maven:3-amazoncorretto-21 as build

WORKDIR /app-server

COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine-jdk

RUN apk update && apk add bash dos2unix

COPY wait-for-it.sh /wait-for-it.sh

RUN \
    dos2unix /wait-for-it.sh &&  \
    chmod +x /wait-for-it.sh

COPY --from=build /app-server/target/*.jar app.jar

CMD ["/bin/sh", "-c", "/wait-for-it.sh postgres:5432 -s -- java -jar app.jar --spring.profiles.active=prod"]
