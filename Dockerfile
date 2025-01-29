#FROM eclipse-temurin:17.0.12_7-jdk-jammy as builder
#WORKDIR /build
#
## gradle 관련 파일 복사 (gradle.properties 제외)
#COPY gradlew .
#COPY gradle gradle
#COPY build.gradle .
#COPY settings.gradle .
#
## 플러그인 리포지토리 설정 추가
#RUN mkdir -p ~/.gradle && \
#    echo "pluginManagement { repositories { gradlePluginPortal() } }" > settings.gradle
#
## 소스 코드 복사
#COPY src src
#
## gradle 권한 설정 및 빌드
#RUN chmod +x ./gradlew
#RUN ./gradlew build -x test --stacktrace --info
#
## 실행 환경
#FROM eclipse-temurin:17.0.12_7-jre-jammy
#WORKDIR /app
#
## SSL 디렉토리 생성
#RUN mkdir -p /app/ssl
#
## SSL 인증서 및 jar 파일 복사
#COPY --from=builder /build/src/main/resources/keystore.p12 /app/ssl/
#COPY --from=builder /build/build/libs/*.jar app.jar
#
#EXPOSE 8443
#ENTRYPOINT ["java", "-jar", "app.jar"]
#


FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /build

# gradle 관련 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 플러그인 리포지토리 설정 추가
RUN mkdir -p ~/.gradle && \
    echo "pluginManagement { repositories { gradlePluginPortal() } }" > settings.gradle

# gradle wrapper 설정
ENV GRADLE_OPTS="-Dhttp.proxyHost=8.8.8.8 -Dhttp.proxyPort=80 -Dhttps.proxyHost=8.8.8.8 -Dhttps.proxyPort=443"
COPY gradle/wrapper/gradle-wrapper.properties .

# 소스 코드 복사
COPY src src

# gradle 권한 설정 및 빌드
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon \
    -Dorg.gradle.internal.http.socketTimeout=120000 \
    -Dorg.gradle.internal.http.connectionTimeout=120000 \
    -Dhttp.proxyHost=8.8.8.8 \
    -Dhttp.proxyPort=80 \
    -Dhttps.proxyHost=8.8.8.8 \
    -Dhttps.proxyPort=443

# 실행 환경
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# SSL 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/ssl && chmod 755 /app/ssl

# SSL 인증서 및 jar 파일 복사
COPY --from=builder /build/src/main/resources/motorcycle.p12 /app/ssl/
COPY --from=builder /build/build/libs/*.jar app.jar

EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]