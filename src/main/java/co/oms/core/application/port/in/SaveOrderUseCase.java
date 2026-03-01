package co.oms.core.application.port.in;

import java.util.List;

/** 주문 저장 유스케이스 */
public interface SaveOrderUseCase {

    /** 주문 커맨드 리스트를 수신하여 저장 */
    void saveOrders(List<SaveOrderCommand> commands);
}
