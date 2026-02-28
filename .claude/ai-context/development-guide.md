# OMS-CORE 개발 가이드

---

## 기술 스택

| 항목 | 기술 | 버전 |
|------|------|------|
| 언어 | Java | 21 |
| 프레임워크 | Spring Boot | 4.0.3 |
| 빌드 | Gradle | 9.3.1 |
| 의존성 관리 | Spring Dependency Management | 1.1.7 |
| 테스트 | JUnit 5 | (Spring Boot 관리) |

---

## 패키지 구조 컨벤션

```
co.oms.omscore/
├── api/                        # API 계층 (외부 노출)
│   ├── controller/             # REST Controller
│   └── dto/                    # Request/Response DTO
├── core/                       # 핵심 도메인 계층
│   ├── domain/                 # 엔티티, VO, Aggregate Root
│   │   └── enums/              # Enum 정의
│   ├── service/                # 비즈니스 로직
│   └── repository/             # Repository 인터페이스
├── infra/                      # 인프라 계층
│   ├── kafka/                  # Kafka Producer/Consumer
│   │   ├── producer/
│   │   └── consumer/
│   ├── client/                 # 외부 API 클라이언트 (WebClient)
│   └── config/                 # 설정 클래스
└── common/                     # 공통 유틸
    ├── exception/              # 예외 정의
    ├── response/               # 공통 응답 포맷
    └── ServerPropertiesController.java
```

---

## 코딩 컨벤션

### Reactive 패턴 (WebFlux)

- `Mono`/`Flux`를 사용한 비동기 처리
- **`.block()` 사용 금지** (테스트 코드 제외)
- 에러 처리: `.onErrorResume()` 또는 `.onErrorMap()` 사용
- 스트림 연산에서 부작용(side effect) 최소화

```java
// Good
public Mono<Order> findOrder(String orderCode) {
    return orderRepository.findByOrderCode(orderCode)
        .switchIfEmpty(Mono.error(new OrderNotFoundException(orderCode)));
}

// Bad - block() 사용 금지
public Order findOrder(String orderCode) {
    return orderRepository.findByOrderCode(orderCode).block();
}
```

### 일반 규칙

- 코드 작성 후 `./gradlew spotlessApply`로 포맷팅
- 새 기능 추가 시 테스트 코드 작성 필수
- Enum 사용 시 `domain-glossary.md` 정의를 따른다
- DTO ↔ Entity 변환은 명시적 메서드로 (MapStruct 또는 정적 팩토리)

### 네이밍 컨벤션

| 대상 | 규칙 | 예시 |
|------|------|------|
| 클래스 | PascalCase | `OrderService`, `OutboundOrder` |
| 메서드 | camelCase | `findByOrderCode()`, `createOutbound()` |
| 상수 | UPPER_SNAKE | `MAX_RETRY_COUNT` |
| 패키지 | lowercase | `co.oms.omscore.core.domain` |
| DTO | 접미사 사용 | `OrderCreateRequest`, `OrderResponse` |

---

## 개발 워크플로우

```
1. 코드 작성/수정
2. ./gradlew spotlessApply   (포맷팅)
3. ./gradlew test            (테스트)
4. ./gradlew build           (빌드)
5. PR 작성 (.claude/ai-context/pr-template.md 참조)
```

---

## 테스트 컨벤션

### 테스트 구조

```
src/test/java/co/oms/omscore/
├── core/
│   ├── domain/          # 단위 테스트 (엔티티, VO)
│   └── service/         # 서비스 테스트 (모킹)
├── api/
│   └── controller/      # API 통합 테스트 (WebTestClient)
└── infra/
    └── kafka/           # Kafka 통합 테스트
```

### 테스트 네이밍

```java
@Test
void 주문생성_정상요청이면_READY상태로_생성된다() { }

@Test
void 주문취소_이미완료된_주문이면_예외발생() { }
```

---

## 로컬 개발 환경

```bash
# 실행
./gradlew bootRun

# 포트: 8081
# 프로파일: local (기본)
```

### 환경 프로파일

| 프로파일 | 용도 | 활성화 방법 |
|---------|------|-----------|
| local | 로컬 개발 | 기본값 |
| dev | 개발 서버 | `--spring.profiles.active=dev` |
| stg | 스테이징 | `--spring.profiles.active=stg` |
| perf | 성능 테스트 | `--spring.profiles.active=perf` |
| prod | 운영 | `--spring.profiles.active=prod` |
