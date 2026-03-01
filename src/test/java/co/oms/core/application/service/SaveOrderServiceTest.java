package co.oms.core.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import co.oms.core.application.port.in.SaveOrderCommand;
import co.oms.core.application.port.out.OrderPersistencePort;
import co.oms.core.domain.model.Orders;
import co.oms.core.fixture.OrderFixture;
import co.oms.core.fixture.SaveOrderCommandFixture;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("주문 저장 함수는")
    class SaveOrdersTest {

        @Test
        @DisplayName("정상 요청이면 일괄 저장된다")
        void 정상요청이면_일괄저장된다() {
            // given
            SaveOrderCommand command = SaveOrderCommandFixture.createWithCode("C-12345");
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
        @DisplayName("중복 주문이면 저장하지 않는다")
        void 중복주문이면_저장하지않는다() {
            // given
            SaveOrderCommand command = SaveOrderCommandFixture.createWithCode("C-12345");
            given(orderPersistencePort.findByClientOrderCodeIn(Set.of("C-12345")))
                    .willReturn(new Orders(List.of(OrderFixture.createWithId("id-1", "C-12345"))));

            // when
            saveOrderService.saveOrders(List.of(command));

            // then
            verify(orderPersistencePort, never()).saveAll(any(Orders.class));
        }

        @Test
        @DisplayName("여러 주문을 일괄 저장한다")
        void 여러주문을_일괄저장한다() {
            // given
            SaveOrderCommand command1 = SaveOrderCommandFixture.createWithCode("C-001");
            SaveOrderCommand command2 = SaveOrderCommandFixture.createWithCode("C-002");
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
        @DisplayName("중복 포함이면 중복 제외하고 저장한다")
        void 중복포함이면_중복제외하고_저장한다() {
            // given
            SaveOrderCommand command1 = SaveOrderCommandFixture.createWithCode("C-001");
            SaveOrderCommand command2 = SaveOrderCommandFixture.createWithCode("C-002");
            given(orderPersistencePort.findByClientOrderCodeIn(Set.of("C-001", "C-002")))
                    .willReturn(new Orders(List.of(OrderFixture.createWithId("id-1", "C-001"))));
            given(orderPersistencePort.saveAll(any(Orders.class)))
                    .willAnswer(inv -> inv.getArgument(0));

            // when
            saveOrderService.saveOrders(List.of(command1, command2));

            // then
            verify(orderPersistencePort, times(1)).saveAll(any(Orders.class));
        }
    }
}
