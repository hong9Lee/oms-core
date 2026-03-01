package co.oms.core.application.port.in;

import java.time.LocalDateTime;
import java.util.List;

/** Kafka 주문 메시지 DTO */
public record OrderMessage(
        String clientOrderCode,
        Long customerId,
        String deliveryPolicy,
        LocalDateTime orderDate,
        List<OrderItemMessage> items) {

    /** 주문 상품 메시지 DTO */
    public record OrderItemMessage(
            String goodsCode,
            String goodsName,
            Integer quantity) {}
}
