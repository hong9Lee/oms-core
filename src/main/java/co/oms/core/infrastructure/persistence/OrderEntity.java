package co.oms.core.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/** 주문 MongoDB Entity */
@Getter
@Builder
@Document("orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String clientOrderCode;

    private Long customerId;
    private String deliveryPolicy;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemEntity> items;
}
