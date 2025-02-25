#FROM eclipse-temurin:17-jdk-jammy AS builder
#WORKDIR /build
#
## gradle 관련 파일 복사
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#
## 플러그인 리포지토리 설정 추가
#RUN mkdir -p ~/.gradle && \
#    echo "pluginManagement { repositories { gradlePluginPortal(); mavenCentral() } }" > settings.gradle
#
## 소스 코드 복사
#COPY src src
#
## gradle 권한 설정 및 빌드
#RUN chmod +x ./gradlew
#RUN --mount=type=cache,target=/root/.gradle \
#    ./gradlew build -x test --no-daemon \
#    -Dorg.gradle.console=plain
#
## 실행 환경
#FROM eclipse-temurin:17-jre-jammy
#WORKDIR /app
#
## SSL 디렉토리 생성 및 권한 설정
#RUN mkdir -p /app/ssl && chmod 755 /app/ssl
#
## SSL 인증서 및 jar 파일 복사
#COPY --from=builder /build/src/main/resources/motorcycle.p12 /app/ssl/
#COPY --from=builder /build/build/libs/*.jar app.jar
#
#EXPOSE 8443
#ENTRYPOINT ["java", "-jar", "app.jar"]


FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /build

# gradle 관련 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src src

# gradle 권한 설정 및 빌드
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew build -x test --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# SSL 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/ssl /app/images && \
    chmod 755 /app/ssl /app/images

# JAR 파일 복사
COPY --from=builder /build/build/libs/*.jar app.jar

EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]