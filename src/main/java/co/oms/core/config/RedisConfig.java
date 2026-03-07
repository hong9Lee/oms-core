package co.oms.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/** Redis Pub/Sub 채널 인프라 설정 — 로컬 전용 */
@Configuration
@Profile("local")
public class RedisConfig {

    public static final String OFFSET_COMMAND_CHANNEL = "offset-command";
    public static final String CONSUMER_COMMAND_CHANNEL = "consumer-command";

    @Bean
    public ChannelTopic offsetCommandTopic() {
        return new ChannelTopic(OFFSET_COMMAND_CHANNEL);
    }

    @Bean
    public ChannelTopic consumerCommandTopic() {
        return new ChannelTopic(CONSUMER_COMMAND_CHANNEL);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
