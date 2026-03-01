package co.oms.core.adapter.in.kafka;

import co.oms.core.application.port.in.SaveOrderCommand;
import org.mapstruct.Mapper;

/** OrderMessage → SaveOrderCommand 변환 Mapper */
@Mapper(componentModel = "spring")
public interface OrderMessageMapper {

    SaveOrderCommand toCommand(OrderMessage message);

    SaveOrderCommand.Item toCommandItem(OrderMessage.OrderItemMessage item);
}
