package mediasoft.ru.backend.product.currency.services.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.product.currency.services.interfaces.CurrencyServiceClient;
import net.minidev.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class CurrencyServiceMock implements CurrencyServiceClient {
    @Override
    @SneakyThrows
    @Cacheable(value = "${caching.currency.name}", keyGenerator = "cacheKeyGenerator")
    public JSONObject getCurrencyExchangeRate() {
        String fileName = "exchange-rate.json";
        InputStream inputStream = CurrencyServiceMock.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) throw new IllegalArgumentException("File not found: " + fileName);
        JSONObject object = new ObjectMapper().readValue(inputStream, JSONObject.class);
        log.info("Got currency exchange rate from mock");
        return object;
    }
}
