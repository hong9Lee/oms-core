# OMS-CORE 데이터 모델

> 현재 oms-core는 초기화 단계입니다. 이 문서는 계획된 데이터 모델을 정의합니다.
> 구현이 진행되면 실제 엔티티에 맞게 업데이트하세요.

---

## 핵심 엔티티 관계도 (계획)

```
Order (주문)
 ├── 1:N → OrderItem (주문 상품)
 ├── 1:N → OutboundOrder (출고 요청)
 │          └── 1:N → Shipment (배송)
 └── N:1 → Customer (고객)
```

---

## 엔티티 상세 (계획)

### Order (주문)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | String | PK (MongoDB ObjectId) |
| clientOrderCode | String | 고객주문번호 (외부 시스템) |
| customerId | Long | 고객 ID |
| deliveryPolicy | DeliveryPolicy | 배송 정책 (DAWN/DAY/NOW) |
| orderDate | LocalDateTime | 주문일시 |
| status | OrderStatus | 주문 상태 |
| items | List\<OrderItem\> | 주문 상품 목록 (embedded) |

### OrderItem (주문 상품)

| 필드 | 타입 | 설명 |
|------|------|------|
| goodsCode | String | 상품 코드 |
| goodsName | String | 상품명 |
| quantity | Integer | 수량 |

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

### OutboundStatus

```java
READY, PRODUCING, COMPLETED, CANCELED
```

### DeliveryPolicy

```java
DAWN, DAY, NOW
```

### OrderStatus

```java
RECEIVED, PROCESSING, COMPLETED, CANCELED
```

### Courier

```java
_1PL, CJDT, LTT
```

---

## 클러스터센터 코드

| 코드 | 설명 |
|------|------|
| CC01 | 김포 물류센터 |
| CC02 | 송파 물류센터 |
