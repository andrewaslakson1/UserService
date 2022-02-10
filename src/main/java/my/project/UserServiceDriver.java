package my.project;

import io.prometheus.client.CollectorRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserServiceDriver {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceDriver.class, args);
    }

    @Bean
    public static CollectorRegistry getRegistryBean() {
        return new PrometheusMetricsExportAutoConfiguration().collectorRegistry();
    }

}
