# 시스템 아키텍처

```mermaid
flowchart LR
    subgraph dev["개발 환경"]
        direction TB
        IDE[IntelliJ IDEA]
        Git[Git Repository]
        IDE -->|코드 푸시| Git
    end

    subgraph github["GitHub Actions"]
        direction TB
        Action[자동화된 워크플로우]
        Build[Gradle Build]
        DockerImage[Docker Image]
        Action -->|빌드/테스트| Build
        Build -->|도커 이미지 생성| DockerImage
    end

    subgraph prod["운영 환경 (Ubuntu)"]
        subgraph network["Docker Network (motorcycle-network)"]
            direction TB
            subgraph front["프론트엔드 컨테이너"]
                Thymeleaf[Thymeleaf Engine]
                Static[Static Resources]
            end

            subgraph back["백엔드 컨테이너 (8443 포트)"]
                Security[Spring Security/JWT]
                API[REST API]
                Service[비즈니스 로직]
                MyBatis[MyBatis]
            end

            subgraph db["데이터베이스 컨테이너"]
                MySQL[MySQL 8.0]
                Volume[Volume: mysql-data]
            end
        end
    end

    Git -->|트리거| Action
    DockerImage -->|자동 배포| prod
    API --> Security
    Security --> Service
    Service --> MyBatis
    MyBatis --> MySQL
    MySQL --> Volume
    Thymeleaf -->|API 요청| API
    ```