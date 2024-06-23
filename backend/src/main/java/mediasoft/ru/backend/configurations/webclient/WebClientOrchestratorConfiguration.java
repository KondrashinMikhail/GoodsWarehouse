package mediasoft.ru.backend.configurations.webclient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rest.orchestrator-service")
public class WebClientOrchestratorConfiguration {
    private String host;

    @Bean
    protected WebClient webClientOrchestrator() {
        return WebClient.builder()
                .baseUrl(host)
                .build();
    }
}
