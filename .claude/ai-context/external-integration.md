# OMS-CORE 외부 시스템 연동

> 구현 진행 시 실제 연동 상세로 업데이트 필요

---

## 연동 시스템 맵

```
              ┌──────────┐
   CMS ──────→│          │──────→ WMS (출고 지시)
 (주문접수)    │ oms-core │──────→ TMS (배송 요청)
              │          │──────→ oms-plan (이벤트)
   WMS ──────→│          │
 (출하완료)    └──────────┘
```

---

## 인바운드 연동 (oms-core가 수신)

### CMS (Commerce System)

| 항목 | 값 |
|------|-----|
| 연동 방식 | REST API 호출 (CMS → oms-core) |
| 용도 | 주문 접수, 주문 변경/취소 요청 |

### WMS (Warehouse Management System)

| 항목 | 값 |
|------|-----|
| 연동 방식 | Kafka 이벤트 구독 |
| 용도 | 출하 완료 이벤트 수신 → 배송 상태 갱신 |

---

## 아웃바운드 연동 (oms-core가 발신)

### WMS

| 항목 | 값 |
|------|-----|
| 연동 방식 | REST API 호출 (oms-core → WMS) |
| 용도 | 출고 지시 요청 |

### TMS (Transportation Management System)

| 항목 | 값 |
|------|-----|
| 연동 방식 | REST API 호출 |
| 용도 | 배송 요청, 운송장 발급 |

### oms-plan

| 항목 | 값 |
|------|-----|
| 연동 방식 | Kafka 이벤트 발행 |
| 용도 | 주문/출고 상태 변경 이벤트 전파 |
| 토픽 | `kafka-spec.json` 참조 |

---

## LIP (Logistics Information Platform)

| 항목 | 값 |
|------|-----|
| 연동 방식 | REST API 호출 |
| 용도 | 상품 마스터 정보 조회 (온도대, 센터별 주문유형 등) |
