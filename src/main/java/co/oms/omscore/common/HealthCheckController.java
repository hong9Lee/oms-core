package co.oms.omscore.common;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public Mono<Map<String, Object>> health() {
        return Mono.just(
                Map.of(
                        "status", "UP",
                        "service", "oms-core",
                        "timestamp", LocalDateTime.now().toString()));
    }

    @GetMapping("/server/properties")
    public Mono<Map<String, String>> serverProperties() {
        return Mono.just(Map.of("version", "0.0.1", "service", "oms-core"));
    }
}
