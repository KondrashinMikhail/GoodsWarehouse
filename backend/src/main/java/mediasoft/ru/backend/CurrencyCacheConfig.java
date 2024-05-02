package mediasoft.ru.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Slf4j
public class CurrencyCacheConfig {
    @Value("${caching.currency.key}")
    private String CACHE_CURRENCY_KEY;
    @Value("${caching.currency.name}")
    private String CACHE_CURRENCY_NAME;

    @CacheEvict(value = "${caching.currency.name}", allEntries = true)
    @Scheduled(fixedDelayString = "${caching.ttl}")
    public void emptyHotelsCache() {
        log.info("Cleared cache: {}", CACHE_CURRENCY_NAME);
    }

    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return (target, method, params) -> CACHE_CURRENCY_KEY;
    }
}
