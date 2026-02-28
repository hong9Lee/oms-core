# OMS-CORE 도메인 개요

---

## 서비스 역할

**oms-core**는 주문 관리 시스템(OMS)의 **주문 엔진**으로, 주문 데이터의 **Single Source of Truth(SSOT)** 역할을 합니다.

- 주문 생성부터 배송 완료까지 전체 주문 이행(Order Fulfillment) 라이프사이클을 관리
- 다른 서비스(oms-plan 등)는 oms-core의 이벤트를 구독하거나 API를 통해 주문 데이터를 조회

---

## 핵심 도메인 개념

### 주문 (Order)

주문의 핵심 흐름:

```
주문 접수 → 출고 요청 → 생산 → 출고 완료 → 배송
```

#### 주문 상태 (OutboundStatus)

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

### 주문 코드 체계

| 코드 | 설명 | 예시 |
|------|------|------|
| outboundOrderCode | 출고요청번호 (SOMS 채번) | "O-20241216-001" |
| clientOrderCode | 고객주문번호 (외부 시스템) | "C-12345" |
| shipOrderKey | 출하문서번호 (WMS) | "SHK-001" |
| invoiceNumber | 운송장번호 | "123456789012" |

---

## 주요 Enum (도메인 용어집 참조)

### OrderType & TemperatureType

- **OrderType**: 주문 유형 숫자 코드 ("210", "270") - DB/API 저장용
- **TemperatureType**: 온도대 Enum (COLD, FROZEN, ROOM) - 비즈니스 로직/UI 표시용
- 상세 매핑은 루트 `.claude/ai-context/domain-glossary.md` 참조

### DeliveryPolicy (배송 정책)

| 값 | 설명 |
|----|------|
| DAWN | 새벽배송 (샛별) |
| DAY | 일반배송 (낮배송) |
| NOW | 컬리나우 (즉시배송) |

### Courier (배송사)

| 코드 | 설명 |
|------|------|
| KURLY | 컬리 자체배송 (샛별배송) |
| CJDT | CJ대한통운 (일반배송) |
| LTT | 롯데택배 (일반배송) |

---

## 1P vs 3P 물류

| 유형 | 설명 |
|------|------|
| 1P (First Party) | 컬리 물류센터에서 직접 처리 |
| 3P (Third Party) | 외부 3PL 센터에서 처리 |

---

## 연동 시스템 요약

| 시스템 | 약어 | 역할 |
|--------|------|------|
| WMS | Warehouse Management System | 창고 관리 (출고 지시/확인) |
| TMS | Transportation Management System | 운송 관리 |
| LIP | Logistics Information Platform | 상품 마스터 |
| CMS | Commerce System | 커머스 주문 연동 |

> 상세 연동 정보는 `external-integration.md` 참조
