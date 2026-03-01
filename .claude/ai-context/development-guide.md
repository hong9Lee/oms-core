# OMS-CORE 개발 가이드

> 공통 컨벤션(아키텍처, 코드 스타일, 빌드, 테스트 네이밍 등)은 **루트 `/develop` 스킬**에 정의되어 있다.
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

### 트랜잭션

- `config/MongoConfig` → `MongoTransactionManager` 빈 등록
- `@ConditionalOnMissingBean(PlatformTransactionManager.class)` 적용
- Embedded MongoDB는 Replica Set 미지원 → 테스트에서는 `TestMongoConfig`(no-op TxManager) 사용
- 통합 테스트 클래스에 `@Import(TestMongoConfig.class)` 필수

---

## oms-core 테스트 구조

> 테스트 네이밍 컨벤션은 `/develop` 스킬의 TEST_CONVENTION을 따른다.

```
src/test/java/co/oms/core/
├── domain/model/              # 단위 테스트 (엔티티, VO)
├── application/service/       # 유스케이스 테스트 (Port.out 모킹)
└── adapter/
    ├── in/
    │   ├── web/               # API 통합 테스트 (MockMvc)
    │   └── kafka/             # Kafka Consumer 통합 테스트
    └── out/
        └── persistence/       # Repository 통합 테스트
```
