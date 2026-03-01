package co.oms.core.application.service;

import co.oms.core.application.port.in.OrderMessage;
import co.oms.core.domain.enums.DeliveryPolicy;
import co.oms.core.domain.enums.OrderStatus;
import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.OrderItem;
import co.oms.core.domain.model.OrderItems;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** OrderMessage → Order 도메인 변환 Mapper */
@Mapper(componentModel = "spring", imports = {OrderStatus.class, DeliveryPolicy.class})
public interface OrderMessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(OrderStatus.RECEIVED)")
    @Mapping(target = "deliveryPolicy",
            expression = "java(message.deliveryPolicy() != null ? DeliveryPolicy.valueOf(message.deliveryPolicy()) : null)")
    Order toDomain(OrderMessage message);

    OrderItem toItemDomain(OrderMessage.OrderItemMessage item);

    /** List<OrderItemMessage> → OrderItems */
    default OrderItems toOrderItems(List<OrderMessage.OrderItemMessage> items) {
        if (items == null) {
            return new OrderItems(List.of());
        }
        return new OrderItems(items.stream()
                                   .map(this::toItemDomain)
                                   .toList());
    }
}
