package co.oms.core.adapter.in.scheduler;

import co.oms.core.adapter.in.kafka.OrderMessage;
import co.oms.core.adapter.in.kafka.OrderMessage.OrderItemMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 로컬 테스트용 주문 메시지 프로듀서 — 1초마다 3건 발행 */
@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class OrderTestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.order-1p}")
    private String topic;

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final AtomicLong sequence = new AtomicLong(0);

    @Scheduled(fixedRate = 1000)
    public void produce() {
        for (int i = 0; i < 3; i++) {
            long seq = sequence.incrementAndGet();
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String clientOrderCode = "TEST-" + timestamp + "-" + seq;

            OrderMessage message = new OrderMessage(
                    clientOrderCode,
                    seq,
                    randomDeliveryPolicy(),
                    LocalDateTime.now(),
                    List.of(new OrderItemMessage("GOODS-" + seq, "테스트상품-" + seq, 1)));

            kafkaTemplate
                    .send(topic, clientOrderCode, message)
                    .whenComplete(
                            (result, ex) -> {
                                if (ex != null) {
                                    log.error(
                                            "주문 테스트 메시지 발행 실패 - clientOrderCode: {}",
                                            clientOrderCode,
                                            ex);
                                } else {
                                    log.info(
                                            "주문 테스트 메시지 발행 - topic: {}, partition: {}, offset: {}, clientOrderCode: {}",
                                            result.getRecordMetadata().topic(),
                                            result.getRecordMetadata().partition(),
                                            result.getRecordMetadata().offset(),
                                            clientOrderCode);
                                }
                            });
        }
    }

    private String randomDeliveryPolicy() {
        String[] policies = {"DAWN", "DAY", "NOW"};
        return policies[(int) (sequence.get() % 3)];
    }
}
