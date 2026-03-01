# OMS-CORE 도메인 개요

---

## 서비스 역할

**oms-core**는 주문 관리 시스템(OMS)의 **주문 엔진**으로, 주문 데이터의 **Single Source of Truth(SSOT)** 역할을 합니다.

- 주문 생성부터 배송 완료까지 전체 주문 이행(Order Fulfillment) 라이프사이클을 관리
- 다른 서비스(oms-plan 등)는 oms-core의 이벤트를 구독하거나 API를 통해 주문 데이터를 조회

---

## 핵심 도메인 개념

### 주문 흐름

```
주문 접수 → 출고 요청 → 생산 → 출고 완료 → 배송
```

### 주문 상태 (OutboundStatus)

```
READY → PRODUCING → COMPLETED
  ↓
CANCELED
```

| 상태 | 설명 |
|------|------|
| READY | 출고 준비 (주문 접수 완료) |
| PRODUCING | 생산 중 (피킹/패킹 진행) |
| COMPLETED | 출고 완료 |
| CANCELED | 취소됨 |

---

## 참조

> 도메인 용어(코드 체계, DeliveryPolicy, Courier, 1P/3P 등)는 oms 프로젝트 `.claude/ai-context/domain-glossary.md` 참조
> 상세 연동 정보는 `external-integration.md` 참조
