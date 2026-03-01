package co.oms.core.application.port.in;

import java.time.LocalDateTime;
import java.util.List;

/** 주문 저장 커맨드 (인프라 독립) */
public record SaveOrderCommand(
        String clientOrderCode,
        Long customerId,
        String deliveryPolicy,
        LocalDateTime orderDate,
        List<Item> items) {

    public record Item(
            String goodsCode,
            String goodsName,
            Integer quantity) {}
}
