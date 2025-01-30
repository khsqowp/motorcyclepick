# 16 Motorcycle

> 오토바이 라이프스타일을 더욱 풍부하게 만들어주는 커뮤니티 플랫폼

## 목차
- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [개발 예정 기능](#-개발-예정-기능)
- [특별한 점](#-특별한-점)
- [프로젝트 비전](#-프로젝트-비전)
- [기술 스택](#-기술-스택)

## 📝 프로젝트 소개

16 Motorcycle은 MBTI 테스트와 같은 방식으로 사용자에게 최적화된 오토바이를 추천해주며, 라이더들의 실제 경험과 정보를 공유할 수 있는 공간을 제공합니다.

## ✨ 주요 기능

### 🎯 맞춤형 오토바이 추천 시스템
- 사용자 선호도 기반 추천
- 가격, 용도, 스타일 등 다양한 기준 적용

### 📸 오토바이 갤러리
- 브랜드/모델별 이미지 관리
- 이미지 업로드 및 승인 시스템
- 인스타그램 계정 연동

### 📊 오토바이 데이터 분석
- 사용자 선호도 통계
- 브랜드/모델별 추천 트렌드
- 시즌별, 예산별 선호도 분석

### 📚 오토바이 용어사전
- 카테고리별 용어 관리
- 용어 요청 및 승인 시스템
- 공개/비공개 용어 구분

### 🔐 사용자 관리
- 회원가입 및 로그인
- 이메일 인증
- 권한별 접근 제어(관리자, 모더레이터, 일반 사용자)

### 🛡️ 보안
- 입력값 검증 및 살균
- XSS 방지
- 보안 이벤트 로깅

## 🚀 개발 예정 기능

### 정보 공유
| 기능 | 설명 |
|------|------|
| 💰 실시간 중고 시세 | 모델별 시세 추이, 연식별 가격 비교, 지역별 매물 현황 |
| 🍽 라이더 맛집 | 라이더들이 추천하는 맛집 정보 |
| 📍 포토스팟 | 인생샷을 위한 포토스팟 공유 |
| ☕ 라이더 카페 | 라이더 친화적인 카페 정보 |
| 🗺 추천 투어 경로 | 검증된 투어 코스 정보 |
| 🔧 정비센터 | 신뢰할 수 있는 정비센터 정보 |

### 커뮤니티
- ⚡ 번개 모임 시스템
- 👥 크루 모집 게시판
- 💬 라이더 간 실시간 소통

## 🌟 특별한 점

### 데이터 기반 시스템
- MBTI 방식의 독특한 오토바이 추천 시스템
- 실시간 데이터 분석을 통한 신뢰성 있는 정보 제공
- 맞춤형 필터링과 검색 기능

### 커뮤니티 중심
- 실제 라이더들의 생생한 경험 공유
- 유저 기반의 신뢰성 있는 정보 검증 시스템
- 오토바이 관련 질문, 정보 공유, 경험담 교류

### 사용자 경험
- 실제 오너들의 리뷰와 평점 제공
- 개인 주행 스타일 기반 분석 서비스
- 다양한 라이딩 관련 콘텐츠 제공

## 💝 프로젝트 비전

16 Motorcycle은 단순한 오토바이 정보 제공을 넘어, 라이더들이 서로의 경험과 정보를 공유하고 소통하며 더 풍부한 라이딩 라이프를 즐길 수 있는 종합 커뮤니티 플랫폼을 목표로 합니다.

## 🛠 기술 스택

### Backend Framework & Build Tool
```markdown
- Spring Boot 2.7.5
  - Spring Security
  - Spring MVC
  - Spring AOP
- Gradle
```

### Database & ORM
```markdown
- MySQL
- MyBatis
  - 동적 SQL
  - 타입 핸들러
  - ResultMap
```

### Security Implementation
```markdown
데이터 보안
- BoardUtils
- SecurityService
- URLValidator

파일 시스템 보안
- FileValidator
- ImageConverterUtil

모니터링 & 로깅
- SecurityLogger
- SecurityAspect
- DependencyChecker
```

### Infrastructure
```markdown
이메일 서비스
- JavaMailSender
- SSL/TLS 보안

데이터 백업
- DatabaseBackupConfig
- 증분 백업 및 압축
```

### Web Security Configuration
```markdown
Spring Security 설정
- SecurityConfig
- CSRF/XSS 방지
- 세션 관리
- CSP

웹 설정
- WebConfig
- BoardConfig
- MyBatisConfig
```