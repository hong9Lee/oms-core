# OMS-CORE

OMS 생태계의 **주문 엔진 (Single Source of Truth)** - 주문 생성부터 배송까지 전체 주문 이행 라이프사이클 관리

---

## AI Context 문서

> **필수**: 이 프로젝트에서 작업하는 모든 AI 세션은 반드시 `.claude/ai-context/` 폴더의 **모든 문서를 읽고 숙지**해야 합니다. 이 문서들은 프로젝트의 도메인, API, 아키텍처를 이해하는 데 필수적인 지식 베이스입니다.

| 문서 | 설명 |
|------|------|
| [api-spec.json](.claude/ai-context/api-spec.json) | REST API 명세 (주문, 출고, 헬스체크 엔드포인트) |
| [kafka-spec.json](.claude/ai-context/kafka-spec.json) | Kafka 토픽 명세 (Producer 3개, Consumer 1개) |
| [domain-overview.md](.claude/ai-context/domain-overview.md) | 서비스 역할, 주문 흐름, OutboundStatus, 주요 Enum |
| [data-model.md](.claude/ai-context/data-model.md) | 도메인 모델 (Order, OrderItem, OutboundOrder, Shipment) |
| [external-integration.md](.claude/ai-context/external-integration.md) | 외부 연동 (CMS, WMS, TMS, LIP, oms-plan) |
| [development-guide.md](.claude/ai-context/development-guide.md) | 개발 명령어, 패키지 구조, 코딩/테스트 컨벤션 |
| [deploy-guide.md](.claude/ai-context/deploy-guide.md) | 배포 준비 6단계, 버전 규칙 |

---

## 배포 준비

**배포 관련 내용은 [deploy-guide.md](.claude/ai-context/deploy-guide.md) 파일을 참고하세요.**
