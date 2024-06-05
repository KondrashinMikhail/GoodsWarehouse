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
@ConfigurationProperties(prefix = "rest.account-service")
public class WebClientAccountConfiguration {
    private String host;

    @Bean
    protected WebClient webClientAccount() {
        return WebClient.builder()
                .baseUrl(host)
                .build();
    }
}
