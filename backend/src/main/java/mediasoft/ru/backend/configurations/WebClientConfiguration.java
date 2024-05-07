package mediasoft.ru.backend.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "currency-service")
public class WebClientConfiguration {
    private String host;
    private Long retryCount;

    @Bean
    protected WebClient webClient() {
        return WebClient.builder()
                .baseUrl(host)
                .build();
    }
}
