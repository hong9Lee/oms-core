# OMS-CORE 개발 가이드

> 공통 컨벤션은 oms 프로젝트 스킬에 정의: `/develop`(빌드, 아키텍처, git), `/convention`(코드 스타일), `/test-guide`(테스트).
> 이 문서는 oms-core **고유 오버라이드**만 기술한다.

---

## oms-core 오버라이드

### 웹 프레임워크

- **Spring MVC (Tomcat)** 기반 — WebFlux 사용하지 않음

### 기술 스택 보충

| 항목 | 기술 | 비고 |
|------|------|------|
| 의존성 관리 | Spring Dependency Management | 1.1.7 |
| 모델 변환 | MapStruct | 1.6.3 + lombok-mapstruct-binding:0.2.0 |
| 테스트 DB | Flapdoodle Embedded MongoDB | spring4x:4.24.0 |
| 테스트 Kafka | spring-kafka-test (@EmbeddedKafka) | |

### 트랜잭션 (MongoDB)

```dsl
COMMON: /convention TRANSACTION 섹션 참조 (@Transactional 적용 규칙, 위치)

RUNTIME:
  CONFIG: config/MongoConfig → MongoTransactionManager 빈 등록
  CONDITIONAL: @ConditionalOnMissingBean(PlatformTransactionManager.class) 적용
  REQUIRES: MongoDB Replica Set (standalone은 트랜잭션 미지원)

TEST:
  RULE: Embedded MongoDB(Flapdoodle)는 Replica Set 미지원 → 트랜잭션 사용 불가
  SOLUTION: TestMongoConfig에서 no-op PlatformTransactionManager 빈 등록
  PATTERN: |
    // src/test/java/.../config/TestMongoConfig.java
    @TestConfiguration → no-op PlatformTransactionManager 빈 등록
    // MongoConfig의 @ConditionalOnMissingBean이 TestMongoConfig 빈을 우선 인식
    // 통합 테스트에 @Import(TestMongoConfig.class) 필수
```

### Entity 어노테이션

```dsl
ANNOTATIONS: [@Getter, @Builder, @Document("{collection}"), @NoArgsConstructor(PROTECTED), @AllArgsConstructor]
NOTE: @Document는 MongoDB Spring Data 어노테이션. 공통 컨벤션의 Entity 패턴에서 persistence 어노테이션 부분을 이 서비스에서는 @Document로 적용
```

### 로컬 인프라 (Docker Compose)

```dsl
FILE: docker-compose.yml
SERVICES:
  mongodb: mongo:7.0 (Replica Set 모드, 트랜잭션 지원)
  kafka: apache/kafka:3.9.0 (KRaft 모드)
COMMANDS:
  START: docker compose up -d
  STOP: docker compose down
  RESET: docker compose down -v
```

### 스케줄링

```dsl
ENABLED: @EnableScheduling (OmsCoreApplication)
LOCAL_ONLY: 테스트 프로듀서 등 @Profile("local") 컴포넌트에서 사용
```

---

## oms-core 테스트 구조

> 테스트 컨벤션(네이밍, @Nested, Fixture, Fake 등)은 `/test-guide` 스킬 참조
