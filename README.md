# oms-core

OMS의 **주문 엔진** — 주문 데이터의 Single Source of Truth (SSOT)

주문 생성부터 배송 완료까지 전체 주문 이행(Order Fulfillment) 라이프사이클을 관리한다.
다른 서비스(oms-plan 등)는 Kafka 이벤트 구독 또는 API를 통해 주문 데이터를 조회한다.

---

## 기술 스택

| 항목 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 (Spring MVC) |
| Database | MongoDB |
| Messaging | Apache Kafka |
| Build | Gradle 9.3.1 |
| Format | Spotless |
| Model Mapping | MapStruct 1.6.3 |
| Test DB | Flapdoodle Embedded MongoDB |
| Test Kafka | spring-kafka-test (@EmbeddedKafka) |

---

## AI Context 구조

> 이 서비스는 Root(oms) 프로젝트의 공통 규칙을 상속받고, 아래 파일로 **서비스 고유 오버라이드**를 정의한다.

| 파일 | 내용 | 로드 시점 |
|------|------|----------|
| `domain-overview.md` | 서비스 역할, 주문 흐름, 상태 전이 | 필수 |
| `data-model.md` | 도메인 모델 (Order, OrderItem 등) | 필수 |
| `development-guide.md` | MongoDB 트랜잭션, Entity 어노테이션 등 | 필수 |
| `api-spec.json` | REST API 명세 | 온디맨드 |
| `kafka-spec.json` | Kafka Consumer 명세 | 온디맨드 |
| `external-integration.md` | 외부 연동 (CMS, WMS, oms-plan) | 온디맨드 |
| `deploy-guide.md` | 배포 정보 | 온디맨드 |

**오버라이드 예시:** Root의 `/convention` 스킬은 Entity 어노테이션을 "서비스별 persistence 어노테이션"으로 일반화한다. 이 서비스에서는 `development-guide.md`에서 `@Document` (MongoDB)를 명시한다.

---

## Root에서 자동 적용되는 기능

Root(oms) 프로젝트 하위에 clone되어 있으므로, 아래 기능이 자동 적용된다. 상세는 [Root README](https://github.com/hong9Lee/oms) 참조.

| 기능 | 이 서비스에서의 효과 |
|------|-------------------|
| **Rules** | `adapter/in/web/` 등 경로 작업 시 해당 스킬 섹션 자동 참조 |
| **Hooks** | Java 파일 수정 후 Spotless 자동 포맷팅, main 직접 커밋 차단 |
| **code-reviewer** | 헥사고날 아키텍처 · 컨벤션 기준 코드 리뷰 (sonnet, 수정 불가) |
| **Auto Memory** | 세션 간 학습 유지 (MongoDB 트랜잭션 설정 등 반복 설명 불필요) |

---

## 로컬 실행

### 1. 인프라 기동 (MongoDB + Kafka)

```bash
docker compose up -d            # MongoDB(27017) + Kafka(9092) 기동
docker compose ps               # 상태 확인
docker compose down             # 종료
docker compose down -v          # 종료 + 볼륨 삭제
```

### 2. 서버 실행

```bash
./gradlew bootRun              # http://localhost:8081 (profile: local)
```

서버 기동 시 `local` 프로파일이 활성화되며, 테스트 프로듀서가 1초마다 `order.1p` 토픽으로 3건의 주문 메시지를 자동 발행한다.

### 3. 테스트 / 빌드

```bash
./gradlew test                  # 전체 테스트 (Embedded Kafka + MongoDB, Docker 불필요)
./gradlew spotlessApply         # 코드 포맷팅
./gradlew build                 # 빌드
```
