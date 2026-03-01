# OMS-CORE 데이터 모델

---

## 핵심 엔티티 관계도

```
Order (주문)                    ← IMPLEMENTED
 ├── 1:N → OrderItem (주문 상품) ← IMPLEMENTED (embedded)
 ├── 1:N → OutboundOrder (출고 요청) ← PLANNED
 │          └── 1:N → Shipment (배송)  ← PLANNED
 └── N:1 → Customer (고객)             ← PLANNED
```

---

## 구현 완료 엔티티

### Order (주문)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | String | PK (MongoDB ObjectId) |
| clientOrderCode | String | 고객주문번호 (외부 시스템) |
| customerId | Long | 고객 ID |
| deliveryPolicy | DeliveryPolicy | 배송 정책 (DAWN/DAY/NOW) |
| orderDate | LocalDateTime | 주문일시 |
| status | OrderStatus | 주문 상태 |
| items | OrderItems (일급 객체) | 주문 상품 목록 (embedded) |

### OrderItem (주문 상품) — record

| 필드 | 타입 | 설명 |
|------|------|------|
| goodsCode | String | 상품 코드 |
| goodsName | String | 상품명 |
| quantity | Integer | 수량 |

---

## 계획된 엔티티

### OutboundOrder (출고 요청)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | String | PK (MongoDB ObjectId) |
| outboundOrderCode | String | 출고요청번호 (OMS 채번) |
| orderId | String | FK → Order |
| clusterCenterCode | String | 클러스터센터 코드 (CC01/CC02) |
| status | OutboundStatus | READY/PRODUCING/COMPLETED/CANCELED |
| courier | Courier | 배송사 (1PL/CJDT/LTT) |

### Shipment (배송)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | String | PK (MongoDB ObjectId) |
| outboundOrderId | String | FK → OutboundOrder |
| shipOrderKey | String | 출하문서번호 (WMS) |
| invoiceNumber | String | 운송장번호 |
| courier | Courier | 배송사 |

---

## 주요 Enum

> 상세 정의는 루트 `.claude/ai-context/domain-glossary.md` 참조

| Enum | 값 |
|------|-----|
| OrderStatus | RECEIVED, PROCESSING, COMPLETED, CANCELED |
| OutboundStatus | READY, PRODUCING, COMPLETED, CANCELED |
| DeliveryPolicy | DAWN, DAY, NOW |
| Courier | _1PL, CJDT, LTT |
