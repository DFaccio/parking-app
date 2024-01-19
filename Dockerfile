FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
COPY ./target/parking-app-1.0.0.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","parking-app-1.0.0.jar"]

