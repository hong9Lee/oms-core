package co.oms.core.domain.model;

import java.util.List;

/** 주문 상품 일급 컬렉션 */
public record OrderItems(List<OrderItem> values) {

    public OrderItems {
        if (values == null) {
            values = List.of();
        }
    }

    public int count() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
