package co.oms.core.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import co.oms.core.config.TestMongoConfig;
import co.oms.core.domain.enums.DeliveryPolicy;
import co.oms.core.domain.enums.OrderStatus;
import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.OrderItem;
import co.oms.core.domain.model.OrderItems;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@Import(TestMongoConfig.class)
@EmbeddedKafka(
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers",
        topics = {"order.1p", "order.3p"})
class OrderPersistenceAdapterTest {

    @Autowired
    private OrderPersistenceAdapter adapter;

    @Nested
    @DisplayName("주문 저장 함수는")
    class SaveTest {

        @Test
        @DisplayName("정상 저장 후 조회 가능하다")
        void 정상저장후_조회가능() {
            Order order = createOrder("C-TEST-001");

            Order saved = adapter.save(order);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getClientOrderCode()).isEqualTo("C-TEST-001");
            assertThat(saved.getDeliveryPolicy()).isEqualTo(DeliveryPolicy.DAWN);
            assertThat(saved.getStatus()).isEqualTo(OrderStatus.RECEIVED);
        }

        @Test
        @DisplayName("주문 상품 포함 저장이 정상 동작한다")
        void 주문상품포함_정상동작() {
            OrderItem item = new OrderItem("G-001", "냉장우유", 2);
            Order order = Order.builder()
                               .clientOrderCode("C-TEST-003")
                               .customerId(1L)
                               .deliveryPolicy(DeliveryPolicy.DAWN)
                               .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                               .status(OrderStatus.RECEIVED)
                               .items(new OrderItems(List.of(item)))
                               .build();

            Order saved = adapter.save(order);

            assertThat(saved.getItems().values()).hasSize(1);
            assertThat(saved.getItems().values().get(0).goodsCode()).isEqualTo("G-001");
        }
    }

    @Nested
    @DisplayName("주문 조회 함수는")
    class FindTest {

        @Test
        @DisplayName("clientOrderCode로 조회 가능하다")
        void clientOrderCode로_조회가능() {
            Order order = createOrder("C-TEST-002");
            adapter.save(order);

            Optional<Order> found = adapter.findByClientOrderCode("C-TEST-002");

            assertThat(found).isPresent();
            assertThat(found.get().getCustomerId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("존재하지 않는 코드면 빈값을 반환한다")
        void 존재하지않는코드면_빈값() {
            Optional<Order> found = adapter.findByClientOrderCode("NOT-EXIST");

            assertThat(found).isEmpty();
        }
    }

    private Order createOrder(String clientOrderCode) {
        return Order.builder()
                    .clientOrderCode(clientOrderCode)
                    .customerId(1L)
                    .deliveryPolicy(DeliveryPolicy.DAWN)
                    .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                    .status(OrderStatus.RECEIVED)
                    .items(new OrderItems(List.of()))
                    .build();
    }
}
