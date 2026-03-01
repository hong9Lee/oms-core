package co.oms.core.application.service;

import co.oms.core.application.port.in.OrderMessage;
import co.oms.core.application.port.in.SaveOrderUseCase;
import co.oms.core.application.port.out.OrderRepository;
import co.oms.core.domain.model.Orders;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 주문 저장 유스케이스 구현 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements SaveOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMessageMapper orderMessageMapper;

    @Override
    @Transactional
    public void consumeAndSave(List<OrderMessage> messages) {
        // 1. 메시지를 도메인 객체로 변환
        Orders orders = new Orders(
                messages.stream()
                        .map(orderMessageMapper::toDomain)
                        .toList());

        // 2. 기존 주문 일괄 조회로 중복 확인
        Set<String> requestedCodes = orders.extractClientOrderCodes();
        Orders existingOrders = orderRepository.findByClientOrderCodeIn(requestedCodes);
        Set<String> existingCodes = existingOrders.extractClientOrderCodes();

        // 3. 중복 주문 로깅
        existingCodes.forEach(code ->
                log.warn("주문 중복 수신 - clientOrderCode: {}", code));

        // 4. 신규 주문만 일괄 저장
        Orders newOrders = orders.excludeByClientOrderCodes(existingCodes);
        if (!newOrders.isEmpty()) {
            orderRepository.saveAll(newOrders);
            newOrders.values().forEach(order ->
                    log.info("주문 저장 완료 - clientOrderCode: {}",
                            order.getClientOrderCode()));
        }
    }
}
