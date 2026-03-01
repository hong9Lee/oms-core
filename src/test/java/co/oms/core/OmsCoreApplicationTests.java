package co.oms.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers",
        topics = {"order.1p", "order.3p"})
class OmsCoreApplicationTests {

    @Test
    void contextLoads() {
    }

}
