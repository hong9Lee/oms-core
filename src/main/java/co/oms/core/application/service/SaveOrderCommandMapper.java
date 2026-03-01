package co.oms.core.application.service;

import co.oms.core.application.port.in.SaveOrderCommand;
import co.oms.core.domain.enums.DeliveryPolicy;
import co.oms.core.domain.enums.OrderStatus;
import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.OrderItem;
import co.oms.core.domain.model.OrderItems;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** SaveOrderCommand → Order 도메인 변환 Mapper */
@Mapper(componentModel = "spring", imports = {OrderStatus.class, DeliveryPolicy.class})
public interface SaveOrderCommandMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(OrderStatus.RECEIVED)")
    @Mapping(target = "deliveryPolicy",
            expression = "java(command.deliveryPolicy() != null ? DeliveryPolicy.valueOf(command.deliveryPolicy()) : null)")
    Order toDomain(SaveOrderCommand command);

    OrderItem toItemDomain(SaveOrderCommand.Item item);

    /** List<Item> → OrderItems */
    default OrderItems toOrderItems(List<SaveOrderCommand.Item> items) {
        if (items == null) {
            return new OrderItems(List.of());
        }
        return new OrderItems(items.stream()
                                   .map(this::toItemDomain)
                                   .toList());
    }
}
