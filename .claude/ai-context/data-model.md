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

Goods (상품 마스터)
 └── 1:N → CenterOrderType (센터별 주문유형 매핑)
```

---

## 엔티티 상세 (계획)

### Order (주문)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| clientOrderCode | String | 고객주문번호 (외부 시스템) |
| customerId | Long | 고객 ID |
| deliveryPolicy | DeliveryPolicy | 배송 정책 (DAWN/DAY/NOW) |
| orderDate | LocalDateTime | 주문일시 |
| status | OrderStatus | 주문 상태 |

### OrderItem (주문 상품)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| orderId | Long | FK → Order |
| goodsCode | String | 상품 코드 |
| quantity | Integer | 수량 |
| temperatureType | TemperatureType | 온도대 |

### OutboundOrder (출고 요청)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| outboundOrderCode | String | 출고요청번호 (SOMS 채번) |
| orderId | Long | FK → Order |
| clusterCenterCode | String | 클러스터센터 코드 |
| orderType | String | 주문유형 코드 ("210", "270" 등) |
| temperatureType | TemperatureType | 온도대 |
| status | OutboundStatus | READY/PRODUCING/COMPLETED/CANCELED |
| courier | Courier | 배송사 |

### Shipment (배송)

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK |
| outboundOrderId | Long | FK → OutboundOrder |
| shipOrderKey | String | 출하문서번호 (WMS) |
| invoiceNumber | String | 운송장번호 |
| courier | Courier | 배송사 |

---

## 주요 Enum

### TemperatureType

```java
COLD, FROZEN, FROZEN_SIOC, ROOM, ROOM_SIOC,
RED, GREEN, GREEN_EXPENSIVE_APPLIANCE_SIOC,
FASHION, FASHION_SIOC
```

> 각 값의 dawnOrderType/dayOrderType 매핑은 루트 `domain-glossary.md` 참조

### OutboundStatus

```java
READY, PRODUCING, COMPLETED, CANCELED
```

### DeliveryPolicy

```java
DAWN, DAY, NOW
```

### Courier

```java
KURLY, CJDT, LTT
```

---

## 클러스터센터 코드

| 코드 | 설명 | 레거시 코드 |
|------|------|------------|
| CC01 | 김포 물류센터 | 2cc |
| CC02 | 송파 물류센터 | 34cc |
