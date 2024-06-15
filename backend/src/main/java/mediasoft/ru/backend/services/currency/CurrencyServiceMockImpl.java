package mediasoft.ru.backend.services.currency;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.models.ExchangeRateDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static mediasoft.ru.backend.configurations.CurrencyCacheConfig.CACHE_CURRENCY_NAME;

@Service
@Slf4j
@Primary
@ConditionalOnProperty(name = "rest.currency-service.mock.enabled", havingValue = "true")
public class CurrencyServiceMockImpl implements CurrencyService {
    @Override
    @SneakyThrows
    @Cacheable(value = CACHE_CURRENCY_NAME)
    public ExchangeRateDTO getCurrencyExchangeRate() {
        ExchangeRateDTO result = ExchangeRateDTO.builder()
                .exchangeRateCNY(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(100)))
                .exchangeRateEUR(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(100)))
                .exchangeRateUSD(BigDecimal.valueOf(Math.random()).multiply(BigDecimal.valueOf(100)))
                .build();
        log.info("Got currency exchange rate from mock and saved in cache");
        return result;
    }
}
