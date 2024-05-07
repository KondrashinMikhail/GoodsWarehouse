package mediasoft.ru.backend.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
public class CurrencyCacheConfig {
    public static final String CACHE_CURRENCY_NAME = "currencyCash";

    @CacheEvict(value = "${app.caching.currency.name}", allEntries = true)
    @Scheduled(fixedDelayString = "${app.caching.ttl}")
    public void emptyCurrencyCache() {
        log.info("Cleared cache: {}", CACHE_CURRENCY_NAME);
    }
}
