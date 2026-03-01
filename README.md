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

## 로컬 실행

```bash
./gradlew bootRun              # http://localhost:8081
./gradlew test                  # 전체 테스트
./gradlew spotlessApply         # 코드 포맷팅
./gradlew build                 # 빌드
```
