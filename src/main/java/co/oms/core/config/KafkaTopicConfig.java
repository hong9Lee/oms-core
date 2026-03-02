package co.oms.core.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

/** Kafka 토픽 자동 생성 — 로컬 전용 (3 파티션) */
@Configuration
@Profile("local")
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderFirstPartyTopic(@Value("${kafka.topics.order-1p}") String topic) {
        return TopicBuilder.name(topic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic orderThirdPartyTopic(@Value("${kafka.topics.order-3p}") String topic) {
        return TopicBuilder.name(topic).partitions(3).replicas(1).build();
    }
}
