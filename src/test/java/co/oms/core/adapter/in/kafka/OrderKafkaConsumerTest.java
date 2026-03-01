package co.oms.core.adapter.in.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import co.oms.core.infrastructure.persistence.OrderEntityRepository;
import co.oms.core.adapter.in.kafka.OrderMessage;
import co.oms.core.config.TestMongoConfig;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;

@SpringBootTest
@Import(TestMongoConfig.class)
@EmbeddedKafka(
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers",
        topics = {"order.1p", "order.3p"})
class OrderKafkaConsumerTest {

    @Autowired
    private OrderEntityRepository repository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Nested
    @DisplayName("메시지 수신 함수는")
    class ConsumeTest {

        @Test
        @DisplayName("1P 토픽 메시지를 수신하면 주문을 저장한다")
        void 카프카메시지수신_주문저장성공() {
            // 1. 파티션 할당 대기
            for (MessageListenerContainer container :
                    kafkaListenerEndpointRegistry.getListenerContainers()) {
                ContainerTestUtils.waitForAssignment(container, 2);
            }

            // 2. 메시지 발행
            OrderMessage message = new OrderMessage(
                    "C-KAFKA-001",
                    100L,
                    "DAWN",
                    LocalDateTime.of(2026, 2, 28, 10, 0),
                    List.of());

            kafkaTemplate.send("order.1p", "C-KAFKA-001", message);

            // 3. 저장 확인
            await().atMost(Duration.ofSeconds(30))
                   .untilAsserted(
                           () -> assertThat(repository.findByClientOrderCode("C-KAFKA-001"))
                                   .isPresent());
        }
    }
}
