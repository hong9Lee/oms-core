package co.oms.core.adapter.in.kafka;

import co.oms.core.application.port.in.OrderMessage;
import co.oms.core.application.port.in.SaveOrderUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** 주문 Kafka Consumer */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaConsumer {

    private final SaveOrderUseCase saveOrderUseCase;

    @KafkaListener(
            topics = {"${kafka.topics.order-1p}", "${kafka.topics.order-3p}"},
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderKafkaListenerContainerFactory")
    public void consume(List<ConsumerRecord<String, OrderMessage>> records) {
        log.info("주문 메시지 수신 - {}건", records.size());
        saveOrderUseCase.consumeAndSave(
                records.stream()
                       .map(ConsumerRecord::value)
                       .toList());
    }
}
