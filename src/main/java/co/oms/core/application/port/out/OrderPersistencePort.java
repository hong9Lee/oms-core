package co.oms.core.application.port.out;

import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.Orders;
import java.util.Optional;
import java.util.Set;

public interface OrderPersistencePort {

    Order save(Order order);

    Orders saveAll(Orders orders);

    Optional<Order> findByClientOrderCode(String clientOrderCode);

    Orders findByClientOrderCodeIn(Set<String> clientOrderCodes);
}
