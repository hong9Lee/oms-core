package co.oms.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OmsCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmsCoreApplication.class, args);
    }

}
