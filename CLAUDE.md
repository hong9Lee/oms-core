# OMS-CORE

OMS 생태계의 **주문 엔진 (Single Source of Truth)** - 주문 생성부터 배송까지 전체 주문 이행 라이프사이클 관리

---

## AI Context 문서

> 로드 시점은 **Backend 역할 가이드**(`.claude/role/backend/README.md`)에서 관리한다.
> 필수 로드 / 온디맨드 로드 구분을 따른다.

| 문서 | 설명 | 로드 시점 |
|------|------|----------|
| [domain-overview.md](.claude/ai-context/domain-overview.md) | 서비스 역할, 주문 흐름, OutboundStatus | 필수 |
| [data-model.md](.claude/ai-context/data-model.md) | 도메인 모델 (Order, OrderItem 등) | 필수 |
| [development-guide.md](.claude/ai-context/development-guide.md) | oms-core 고유 기술 스택, 트랜잭션 | 필수 |
| [api-spec.json](.claude/ai-context/api-spec.json) | REST API 명세 | 온디맨드 |
| [kafka-spec.json](.claude/ai-context/kafka-spec.json) | Kafka Consumer 명세 | 온디맨드 |
| [external-integration.md](.claude/ai-context/external-integration.md) | 외부 연동 (CMS, WMS, oms-plan) | 온디맨드 |
| [deploy-guide.md](.claude/ai-context/deploy-guide.md) | 배포 정보 | 온디맨드 |
