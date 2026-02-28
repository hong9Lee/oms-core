# oms-core

OMS(Order Management System)의 **주문 엔진** — 주문 데이터의 Single Source of Truth(SSOT)

## 서비스 역할

- 주문 생성부터 배송 완료까지 전체 주문 이행(Order Fulfillment) 라이프사이클 관리
- 다른 서비스(oms-plan 등)는 이벤트 구독 또는 API를 통해 주문 데이터 조회

## 기술 스택

| 항목 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 (WebFlux) |
| Build | Gradle 9.3.1 |
| Messaging | Kafka |

## 로컬 실행

```bash
./gradlew bootRun
# http://localhost:8081
```

## 개발 워크플로우

```bash
./gradlew spotlessApply   # 코드 포맷팅
./gradlew test            # 테스트
./gradlew build           # 빌드
```
