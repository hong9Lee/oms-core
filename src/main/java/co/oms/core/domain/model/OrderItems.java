package co.oms.core.domain.model;

import java.util.List;
import java.util.Objects;

/** 주문 상품 일급 컬렉션 */
public record OrderItems(List<OrderItem> values) {

    public OrderItems {
        Objects.requireNonNull(values, "values must not be null");
        values = List.copyOf(values);
    }

    public int count() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
