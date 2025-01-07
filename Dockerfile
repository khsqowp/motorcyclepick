# Dockerfile
FROM openjdk:17
WORKDIR /app

RUN mkdir -p /app/src/main/resources/static/images

COPY build/libs/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/app/app.jar"]