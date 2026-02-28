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

## 아키텍처: Hexagonal (Ports & Adapters)

모든 OMS 서비스는 헥사고날 아키텍처를 따른다.

### 핵심 원칙

- **도메인은 외부를 모른다**: domain 패키지는 infra, adapter에 의존하지 않는다
- **Port**: 도메인이 정의하는 인터페이스 (in: 유스케이스, out: 외부 연동)
- **Adapter**: Port의 구현체 (in: Controller/Consumer, out: Repository/Client)
- **의존성 방향**: adapter → application → domain (항상 안쪽으로)

### 패키지 구조

```
co.oms.core/
├── adapter/                           # Adapter 계층 (외부와의 접점)
│   ├── in/                            # Inbound Adapter (외부 → 도메인)
│   │   ├── web/                       # REST Controller + Request/Response DTO
│   │   └── kafka/                     # Kafka Consumer
│   └── out/                           # Outbound Adapter (도메인 → 외부)
│       ├── persistence/               # DB Repository 구현체
│       ├── kafka/                     # Kafka Producer
│       └── client/                    # 외부 API 클라이언트 (RestClient)
├── application/                       # Application 계층 (유스케이스 조합)
│   ├── port/                          # Port 인터페이스
│   │   ├── in/                        # Inbound Port (유스케이스 인터페이스)
│   │   └── out/                       # Outbound Port (외부 연동 인터페이스)
│   └── service/                       # 유스케이스 구현 (Port.in 구현체)
├── domain/                            # Domain 계층 (순수 비즈니스 로직)
│   ├── model/                         # 엔티티, VO, Aggregate Root
│   └── enums/                         # Enum 정의
├── config/                            # 설정 클래스
└── common/                            # 공통 유틸 (예외, 응답 포맷, 헬스체크)
```

### 의존성 규칙

```
adapter.in.web → application.port.in → application.service → application.port.out
                                                                      ↑
                                                            adapter.out.persistence (구현)
```

| 패키지 | 의존 가능 | 의존 불가 |
|--------|----------|----------|
| domain | 없음 (순수) | application, adapter, infra |
| application | domain | adapter |
| adapter | application, domain | 다른 adapter |

---

## 코딩 컨벤션

코드 컨벤션, 네이밍 규칙, 클래스 구조, 빌드 명령어 등 상세 개발 가이드는 **`/develop` 스킬**을 참조한다.

핵심 사항만 요약:
- oms-core는 **Spring MVC (Tomcat)** 기반 (WebFlux 사용하지 않음)
- DI: `@RequiredArgsConstructor` + `final` 필드
- `@Getter` 사용 가능, `@Setter` 절대 사용 금지
- Java `record`를 사용할 수 있는 곳이면 무조건 사용
- 설정값은 `application.yml`에서 `@Value`로 주입 (하드코딩 금지)

---

## 테스트 컨벤션

### 테스트 구조

```
src/test/java/co/oms/core/
├── domain/
│   └── model/              # 단위 테스트 (엔티티, VO)
├── application/
│   └── service/            # 유스케이스 테스트 (Port.out 모킹)
└── adapter/
    ├── in/
    │   └── web/            # API 통합 테스트 (MockMvc)
    └── out/
        ├── persistence/    # Repository 통합 테스트
        └── kafka/          # Kafka 통합 테스트
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
