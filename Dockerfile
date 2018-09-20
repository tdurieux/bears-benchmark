FROM node as frontend

WORKDIR /app

ENV ANGULAR_CLI_VERSION="6.0.8"

RUN npm install -g @angular/cli@$ANGULAR_CLI_VERSION

COPY src/main/angular/package.json /app
RUN npm install

COPY src/main/angular /app
RUN ng build --aot --prod

FROM maven:3.5.4-jdk-8-slim as backend

WORKDIR /app

COPY pom.xml /app
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

COPY . /app
COPY --from=frontend app/dist/patiolegal src/main/resources/static
RUN mvn clean install -Dmaven.antrun.skip=true

FROM openjdk:8u171-jdk-slim

WORKDIR /app

ENV SPRING_DATA_MONGODB_URI=
ENV JAVA_OPTS=

COPY --from=frontend app/dist/patiolegal /app/patiolegal/static
COPY --from=backend app/target/patiolegal.jar /app

ENTRYPOINT java $JAVA_OPTS -Dspring.data.mongodb.uri=$SPRING_DATA_MONGODB_URI -jar patiolegal.jar