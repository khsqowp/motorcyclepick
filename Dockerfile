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
COPY gradle/wrapper/gradle-wrapper.properties .

# 소스 코드 복사
COPY src src

# gradle 권한 설정 및 빌드
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew build -x test --no-daemon \
    --refresh-dependencies \
    -Dorg.gradle.internal.http.socketTimeout=180000 \
    -Dorg.gradle.internal.http.connectionTimeout=180000 \
    -Dorg.gradle.console=plain \
    -Dhttps.protocols=TLSv1.2,TLSv1.3 \
    -Djdk.http.auth.tunneling.disabledSchemes="" \
    -Dgradle.user.home=/root/.gradle \
    -Djava.net.preferIPv4Stack=true

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
#    echo "pluginManagement { repositories { gradlePluginPortal() } }" > settings.gradle
#
## gradle wrapper 설정
#COPY gradle/wrapper/gradle-wrapper.properties .
#RUN ./gradlew wrapper --gradle-version 7.6.1 --no-daemon || true
#
## 소스 코드 복사
#COPY src src
#
## gradle 권한 설정 및 빌드
#RUN chmod +x ./gradlew
#RUN --mount=type=cache,target=/root/.gradle \
#    ./gradlew build -x test --no-daemon \
#    -Dorg.gradle.internal.http.socketTimeout=120000 \
#    -Dorg.gradle.internal.http.connectionTimeout=120000 \
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