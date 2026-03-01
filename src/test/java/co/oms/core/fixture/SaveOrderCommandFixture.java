package co.oms.core.fixture;

import co.oms.core.application.port.in.SaveOrderCommand;
import java.time.LocalDateTime;
import java.util.List;

public class SaveOrderCommandFixture {

    public static SaveOrderCommand createDefault() {
        return new SaveOrderCommand(
                "C-TEST-001",
                1L,
                "DAWN",
                LocalDateTime.of(2026, 2, 28, 10, 0),
                List.of());
    }

    public static SaveOrderCommand createWithCode(String clientOrderCode) {
        return new SaveOrderCommand(
                clientOrderCode,
                1L,
                "DAWN",
                LocalDateTime.of(2026, 2, 28, 10, 0),
                List.of());
    }
}
