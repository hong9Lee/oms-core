package co.oms.core.fixture;

import co.oms.core.adapter.in.kafka.OrderMessage;
import java.time.LocalDateTime;
import java.util.List;

public class OrderMessageFixture {

    public static OrderMessage createDefault() {
        return new OrderMessage(
                "C-KAFKA-001",
                100L,
                "DAWN",
                LocalDateTime.of(2026, 2, 28, 10, 0),
                List.of());
    }

    public static OrderMessage createWithCode(String clientOrderCode) {
        return new OrderMessage(
                clientOrderCode,
                100L,
                "DAWN",
                LocalDateTime.of(2026, 2, 28, 10, 0),
                List.of());
    }
}
