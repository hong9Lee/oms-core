package co.oms.core.application.service;

import co.oms.core.application.port.in.OrderMessage;
import co.oms.core.application.port.in.SaveOrderUseCase;
import co.oms.core.application.port.out.OrderPersistencePort;
import co.oms.core.domain.model.Orders;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 주문 저장 유스케이스 구현 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SaveOrderService implements SaveOrderUseCase {

    private final OrderPersistencePort orderPersistencePort;
    private final OrderMessageMapper orderMessageMapper;

    @Override
    @Transactional
    public void consumeAndSave(List<OrderMessage> messages) {
        Orders orders = this.toDomainOrders(messages);
        Orders newOrders = this.filterNewOrders(orders);

        if (newOrders.isEmpty()) {
            return;
        }

        orderPersistencePort.saveAll(newOrders);
        this.logSaved(newOrders);
    }

    /** 메시지 → 도메인 변환 */
    private Orders toDomainOrders(List<OrderMessage> messages) {
        return new Orders(
                messages.stream()
                        .map(orderMessageMapper::toDomain)
                        .toList());
    }

    /** 기존 주문 제외한 신규 주문만 필터링 */
    private Orders filterNewOrders(Orders orders) {
        Orders existingOrders = orderPersistencePort.findByClientOrderCodeIn(
                orders.extractClientOrderCodes());
        this.logDuplicates(existingOrders);
        return orders.excludeExisting(existingOrders);
    }

    /** 중복 주문 로깅 */
    private void logDuplicates(Orders duplicates) {
        duplicates.values().forEach(order ->
                log.warn("주문 중복 수신 - clientOrderCode: {}", order.getClientOrderCode()));
    }

    /** 저장 완료 로깅 */
    private void logSaved(Orders saved) {
        saved.values().forEach(order ->
                log.info("주문 저장 완료 - clientOrderCode: {}", order.getClientOrderCode()));
    }
}
