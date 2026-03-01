package co.oms.core.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import co.oms.core.application.port.in.SaveOrderCommand;
import co.oms.core.application.port.out.OrderPersistencePort;
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
class SaveOrderServiceTest {

    @Mock
    private OrderPersistencePort orderPersistencePort;

    @Spy
    private SaveOrderCommandMapper saveOrderCommandMapper =
            Mappers.getMapper(SaveOrderCommandMapper.class);

    @InjectMocks
    private SaveOrderService saveOrderService;

    @Test
    void 주문저장_정상요청이면_일괄저장된다() {
        // given
        SaveOrderCommand command = this.createCommand("C-12345");
        given(orderPersistencePort.findByClientOrderCodeIn(Set.of("C-12345")))
                .willReturn(new Orders(List.of()));
        given(orderPersistencePort.saveAll(any(Orders.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        saveOrderService.saveOrders(List.of(command));

        // then
        verify(orderPersistencePort, times(1)).saveAll(any(Orders.class));
    }

    @Test
    void 주문저장_중복주문이면_저장하지않는다() {
        // given
        SaveOrderCommand command = this.createCommand("C-12345");
        Order existing = Order.builder()
                              .id("id-1")
                              .clientOrderCode("C-12345")
                              .customerId(1L)
                              .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                              .items(new OrderItems(List.of()))
                              .build();
        given(orderPersistencePort.findByClientOrderCodeIn(Set.of("C-12345")))
                .willReturn(new Orders(List.of(existing)));

        // when
        saveOrderService.saveOrders(List.of(command));

        // then
        verify(orderPersistencePort, never()).saveAll(any(Orders.class));
    }

    @Test
    void 배치주문저장_여러주문을_일괄저장한다() {
        // given
        SaveOrderCommand command1 = this.createCommand("C-001");
        SaveOrderCommand command2 = this.createCommand("C-002");
        given(orderPersistencePort.findByClientOrderCodeIn(Set.of("C-001", "C-002")))
                .willReturn(new Orders(List.of()));
        given(orderPersistencePort.saveAll(any(Orders.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        saveOrderService.saveOrders(List.of(command1, command2));

        // then
        verify(orderPersistencePort, times(1)).saveAll(any(Orders.class));
    }

    @Test
    void 배치주문저장_중복포함이면_중복제외하고_저장한다() {
        // given
        SaveOrderCommand command1 = this.createCommand("C-001");
        SaveOrderCommand command2 = this.createCommand("C-002");
        Order existing = Order.builder()
                              .id("id-1")
                              .clientOrderCode("C-001")
                              .customerId(1L)
                              .orderDate(LocalDateTime.of(2026, 2, 28, 10, 0))
                              .items(new OrderItems(List.of()))
                              .build();
        given(orderPersistencePort.findByClientOrderCodeIn(Set.of("C-001", "C-002")))
                .willReturn(new Orders(List.of(existing)));
        given(orderPersistencePort.saveAll(any(Orders.class)))
                .willAnswer(inv -> inv.getArgument(0));

        // when
        saveOrderService.saveOrders(List.of(command1, command2));

        // then
        verify(orderPersistencePort, times(1)).saveAll(any(Orders.class));
    }

    private SaveOrderCommand createCommand(String clientOrderCode) {
        return new SaveOrderCommand(
                clientOrderCode,
                1L,
                "DAWN",
                LocalDateTime.of(2026, 2, 28, 10, 0),
                List.of());
    }
}
