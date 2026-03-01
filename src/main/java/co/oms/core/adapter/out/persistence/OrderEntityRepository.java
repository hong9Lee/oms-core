package co.oms.core.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;

/** 주문 MongoDB Repository */
public interface OrderEntityRepository extends MongoRepository<OrderEntity, String> {

    Optional<OrderEntity> findByClientOrderCode(String clientOrderCode);

    List<OrderEntity> findByClientOrderCodeIn(Set<String> clientOrderCodes);
}
