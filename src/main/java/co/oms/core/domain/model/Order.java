package co.oms.core.domain.model;

import co.oms.core.domain.enums.DeliveryPolicy;
import co.oms.core.domain.enums.OrderStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/** 주문 Aggregate Root */
@Getter
@Builder
@AllArgsConstructor
public class Order {

    private final String id;
    private final String clientOrderCode;
    private final Long customerId;
    private final DeliveryPolicy deliveryPolicy;
    private final LocalDateTime orderDate;
    private final OrderStatus status;
    private final OrderItems items;
}
