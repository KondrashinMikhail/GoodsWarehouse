package mediasoft.ru.backend.services.currency;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.configurations.rest.RestConfiguration;
import mediasoft.ru.backend.models.ExchangeRateDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static mediasoft.ru.backend.configurations.CurrencyCacheConfig.CACHE_CURRENCY_NAME;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyServiceClientImpl implements CurrencyService {
    private final WebClient webClientCurrency;
    private final RestConfiguration restConfiguration;

    @Override
    @SneakyThrows
    @Cacheable(value = CACHE_CURRENCY_NAME)
    public ExchangeRateDTO getCurrencyExchangeRate() {
        try {
            ExchangeRateDTO response = webClientCurrency
                    .get()
                    .uri(restConfiguration.getCurrencyService().getMethods().getGetCurrency())
                    .retrieve()
                    .bodyToMono(ExchangeRateDTO.class)
                    .retry(restConfiguration.getCurrencyService().getRetryCount())
                    .block();
            log.info("Got currency exchange rate from another service and saved in cache");
            return response;
        } catch (Exception exception) {
            return null;
        }
    }
}
