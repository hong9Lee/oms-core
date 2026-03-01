package co.oms.core.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import co.oms.core.application.port.in.OrderMessage;
import co.oms.core.application.port.out.OrderRepository;
import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.OrderItems;
import co.oms.core.domain.model.Orders;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Spy
    private OrderMessageMapper orderMessageMapper = Mappers.getMapper(OrderMessageMapper.class);

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문저장_정상요청이면_일괄저장된다() {
        // given
        OrderMessage message = this.createMessage("C-12345");
        given(orderRepository.findByClientOrderCodeIn(Set.of("C-12345")))
                .willReturn(new Orders(List.of()));
        given(orderRepository.saveAll(any(Orders.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        orderService.consumeAndSave(List.of(message));

        // then
        verify(orderRepository, times(1)).saveAll(any(Orders.class));
    }

    @Test
    void 주문저장_중복주문이면_저장하지않는다() {
        // given
        OrderMessage message = this.createMessage("C-12345");
        Order existing = Order.builder()
                              .id("id-1")
                              .clientOrderCode("C-12345")
                              .customerId(1L)
                              .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                              .items(new OrderItems(List.of()))
                              .build();
        given(orderRepository.findByClientOrderCodeIn(Set.of("C-12345")))
                .willReturn(new Orders(List.of(existing)));

        // when
        orderService.consumeAndSave(List.of(message));

        // then
        verify(orderRepository, never()).saveAll(any(Orders.class));
    }

    @Test
    void 배치주문저장_여러주문을_일괄저장한다() {
        // given
        OrderMessage message1 = this.createMessage("C-001");
        OrderMessage message2 = this.createMessage("C-002");
        given(orderRepository.findByClientOrderCodeIn(Set.of("C-001", "C-002")))
                .willReturn(new Orders(List.of()));
        given(orderRepository.saveAll(any(Orders.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        orderService.consumeAndSave(List.of(message1, message2));

        // then
        verify(orderRepository, times(1)).saveAll(any(Orders.class));
    }

    @Test
    void 배치주문저장_중복포함이면_중복제외하고_저장한다() {
        // given
        OrderMessage message1 = this.createMessage("C-001");
        OrderMessage message2 = this.createMessage("C-002");
        Order existing = Order.builder()
                              .id("id-1")
                              .clientOrderCode("C-001")
                              .customerId(1L)
                              .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                              .items(new OrderItems(List.of()))
                              .build();
        given(orderRepository.findByClientOrderCodeIn(Set.of("C-001", "C-002")))
                .willReturn(new Orders(List.of(existing)));
        given(orderRepository.saveAll(any(Orders.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        orderService.consumeAndSave(List.of(message1, message2));

        // then
        verify(orderRepository, times(1)).saveAll(any(Orders.class));
    }

    private OrderMessage createMessage(String clientOrderCode) {
        return new OrderMessage(
                clientOrderCode,
                1L,
                "DAWN",
                LocalDateTime.of(2026, 2, 28, 10, 0),
                List.of());
    }
}
