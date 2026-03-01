package co.oms.core.fixture;

import co.oms.core.domain.enums.DeliveryPolicy;
import co.oms.core.domain.enums.OrderStatus;
import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.OrderItem;
import co.oms.core.domain.model.OrderItems;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFixture {

    public static Order createDefault() {
        return Order.builder()
                    .clientOrderCode("C-TEST-001")
                    .customerId(1L)
                    .deliveryPolicy(DeliveryPolicy.DAWN)
                    .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                    .status(OrderStatus.RECEIVED)
                    .items(new OrderItems(List.of()))
                    .build();
    }

    public static Order createWithCode(String clientOrderCode) {
        return Order.builder()
                    .clientOrderCode(clientOrderCode)
                    .customerId(1L)
                    .deliveryPolicy(DeliveryPolicy.DAWN)
                    .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                    .status(OrderStatus.RECEIVED)
                    .items(new OrderItems(List.of()))
                    .build();
    }

    public static Order createWithId(String id, String clientOrderCode) {
        return Order.builder()
                    .id(id)
                    .clientOrderCode(clientOrderCode)
                    .customerId(1L)
                    .deliveryPolicy(DeliveryPolicy.DAWN)
                    .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                    .status(OrderStatus.RECEIVED)
                    .items(new OrderItems(List.of()))
                    .build();
    }

    public static Order createWithItems(String clientOrderCode, List<OrderItem> items) {
        return Order.builder()
                    .clientOrderCode(clientOrderCode)
                    .customerId(1L)
                    .deliveryPolicy(DeliveryPolicy.DAWN)
                    .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                    .status(OrderStatus.RECEIVED)
                    .items(new OrderItems(items))
                    .build();
    }
}
