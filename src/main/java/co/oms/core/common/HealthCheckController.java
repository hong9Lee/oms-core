package co.oms.core.common;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "service", "oms-core",
                "timestamp", LocalDateTime.now().toString());
    }

    @GetMapping("/server/properties")
    public Map<String, String> serverProperties() {
        return Map.of("version", "0.0.1", "service", "oms-core");
    }
}
