package co.oms.core.adapter.out.persistence;

import co.oms.core.domain.enums.DeliveryPolicy;
import co.oms.core.domain.enums.OrderStatus;
import co.oms.core.domain.model.Order;
import co.oms.core.domain.model.OrderItem;
import co.oms.core.domain.model.OrderItems;
import co.oms.core.infrastructure.persistence.OrderEntity;
import co.oms.core.infrastructure.persistence.OrderItemEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/** Order 도메인 ↔ OrderEntity 변환 Mapper */
@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {

    @Mapping(target = "deliveryPolicy", source = "deliveryPolicy", qualifiedByName = "deliveryPolicyToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "orderStatusToString")
    OrderEntity toEntity(Order order);

    OrderItemEntity toItemEntity(OrderItem item);

    @Mapping(target = "deliveryPolicy", source = "deliveryPolicy", qualifiedByName = "stringToDeliveryPolicy")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToOrderStatus")
    Order toDomain(OrderEntity entity);

    OrderItem toItemDomain(OrderItemEntity entity);

    // --- Enum 변환 ---

    @Named("deliveryPolicyToString")
    default String deliveryPolicyToString(DeliveryPolicy policy) {
        return policy != null ? policy.name() : null;
    }

    @Named("stringToDeliveryPolicy")
    default DeliveryPolicy stringToDeliveryPolicy(String value) {
        return value != null ? DeliveryPolicy.valueOf(value) : null;
    }

    @Named("orderStatusToString")
    default String orderStatusToString(OrderStatus status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToOrderStatus")
    default OrderStatus stringToOrderStatus(String value) {
        return value != null ? OrderStatus.valueOf(value) : null;
    }

    // --- 일급 컬렉션 변환 ---

    /** OrderItems → List<OrderItemEntity> */
    default List<OrderItemEntity> toItemEntities(OrderItems items) {
        if (items == null) {
            return null;
        }
        return items.values().stream()
                             .map(this::toItemEntity)
                             .toList();
    }

    /** List<OrderItemEntity> → OrderItems */
    default OrderItems toOrderItems(List<OrderItemEntity> entities) {
        if (entities == null) {
            return new OrderItems(List.of());
        }
        return new OrderItems(entities.stream()
                                      .map(this::toItemDomain)
                                      .toList());
    }
}
