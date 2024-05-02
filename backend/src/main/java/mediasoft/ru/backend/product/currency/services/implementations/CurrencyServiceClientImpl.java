package mediasoft.ru.backend.product.currency.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.product.currency.services.interfaces.CurrencyServiceClient;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceClientImpl implements CurrencyServiceClient {
    private final CurrencyServiceMock currencyServiceMock;

    @Value("${currency-service.host}")
    private String HOST;
    @Value("${currency-service.methods.get-currency}")
    private String METHOD;
    @Value("${web-client.retry-count}")
    private Integer RETRY_COUNT;

    @Override
    @Cacheable(value = "${caching.currency.name}", keyGenerator = "cacheKeyGenerator")
    public JSONObject getCurrencyExchangeRate() {
        try {
            Mono<JSONObject> response = WebClient
                    .create(HOST)
                    .get()
                    .uri(METHOD)
                    .retrieve()
                    .bodyToMono(JSONObject.class)
                    .retry(RETRY_COUNT);
            JSONObject object = new ObjectMapper().convertValue(response.block(), JSONObject.class);
            log.info("Got currency exchange rate from another service");
            return object;
        } catch (Exception exception) {
            return currencyServiceMock.getCurrencyExchangeRate();
        }
    }
}
