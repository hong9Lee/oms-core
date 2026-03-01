package co.oms.core.adapter.out.persistence;

import co.oms.core.application.port.out.OrderPersistencePort;
import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.Orders;
import co.oms.core.infrastructure.persistence.OrderEntityRepository;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** OrderPersistencePort 구현체 (MongoDB) */
@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {

    private final OrderEntityRepository orderEntityRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Order save(Order order) {
        var entity = orderPersistenceMapper.toEntity(order);
        var saved = orderEntityRepository.save(entity);
        return orderPersistenceMapper.toDomain(saved);
    }

    @Override
    public Orders saveAll(Orders orders) {
        var entities = orders.values().stream()
                                      .map(orderPersistenceMapper::toEntity)
                                      .toList();
        var saved = orderEntityRepository.saveAll(entities);
        return new Orders(saved.stream()
                               .map(orderPersistenceMapper::toDomain)
                               .toList());
    }

    @Override
    public Optional<Order> findByClientOrderCode(String clientOrderCode) {
        return orderEntityRepository.findByClientOrderCode(clientOrderCode)
                                    .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public Orders findByClientOrderCodeIn(Set<String> clientOrderCodes) {
        var entities = orderEntityRepository.findByClientOrderCodeIn(clientOrderCodes);
        return new Orders(entities.stream()
                                  .map(orderPersistenceMapper::toDomain)
                                  .toList());
    }
}
