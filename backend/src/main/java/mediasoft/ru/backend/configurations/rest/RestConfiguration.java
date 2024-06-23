package mediasoft.ru.backend.configurations.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestConfiguration {
    private RestServiceInfo currencyService;
    private RestServiceInfo crmService;
    private RestServiceInfo accountService;
    private RestServiceInfo orchestratorService;
}
