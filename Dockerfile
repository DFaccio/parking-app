#
# Build stage
#
FROM maven:3.9.6-amazoncorretto-17 AS build

WORKDIR /parking

COPY src /parking/src
COPY pom.xml /parking/pom.xml
COPY src/main/resources/application.properties /parking/application.properties

RUN mvn -f /parking/pom.xml clean package -DskipTests

#
# Package stage
#
FROM amazoncorretto:17-alpine-jdk

WORKDIR /parking

COPY --from=build /parking/target/*.jar /parking/parking-app.jar
COPY --from=build /parking/application.properties /parking/application.properties

EXPOSE 8080

ENTRYPOINT ["java","-jar","parking-app.jar"]
