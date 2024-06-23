package mediasoft.ru.backend.configurations;

import mediasoft.ru.exceptionsstarter.ExceptionsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionsHandlerConfig {
    @Bean
    public ExceptionsHandler exceptionsHandler() {
        return new ExceptionsHandler();
    }
}
