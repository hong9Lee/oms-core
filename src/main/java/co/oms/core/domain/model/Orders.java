package co.oms.core.domain.model;

import co.oms.core.domain.enums.OrderStatus;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** 주문 일급 컬렉션 */
public record Orders(List<Order> values) {

    public Orders {
        values = (values != null) ? List.copyOf(values) : List.of();
    }

    /** clientOrderCode 목록 추출 */
    public Set<String> extractClientOrderCodes() {
        return values.stream()
                     .map(Order::getClientOrderCode)
                     .collect(Collectors.toUnmodifiableSet());
    }

    /** 기존 주문에 포함된 주문을 제외 */
    public Orders excludeExisting(Orders existing) {
        Set<String> existingCodes = existing.extractClientOrderCodes();
        return new Orders(
                values.stream()
                      .filter(order -> !existingCodes.contains(order.getClientOrderCode()))
                      .toList());
    }

    public Orders filterByStatus(OrderStatus status) {
        return new Orders(
                values.stream()
                      .filter(order -> order.getStatus() == status)
                      .toList());
    }

    public int count() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
