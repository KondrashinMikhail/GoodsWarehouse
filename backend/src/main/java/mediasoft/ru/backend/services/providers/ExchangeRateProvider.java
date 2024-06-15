package mediasoft.ru.backend.services.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.enums.CurrencyEnum;
import mediasoft.ru.backend.models.ExchangeRateDTO;
import mediasoft.ru.backend.services.currency.CurrencyService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class ExchangeRateProvider {
    private CurrencyService currencyService;

    public BigDecimal getExchangeRate(CurrencyEnum currency) {
        return Optional.ofNullable(getExchangeRateFromClient(currency))
                .orElseGet(() -> getExchangeRateFromFile(currency));
    }

    private BigDecimal getExchangeRateFromClient(CurrencyEnum currency) {
        return Optional.ofNullable(currencyService.getCurrencyExchangeRate())
                .map(exchangeRate -> getExchangeRateByCurrency(exchangeRate, currency)).orElse(null);
    }

    @SneakyThrows
    private BigDecimal getExchangeRateFromFile(CurrencyEnum currency) {
        String fileName = "exchange-rate.json";
        ExchangeRateDTO object = new ObjectMapper().readValue(new ClassPathResource(fileName).getInputStream(), ExchangeRateDTO.class);
        log.info("Got currency exchange rate from file");
        return getExchangeRateByCurrency(object, currency);
    }

    private BigDecimal getExchangeRateByCurrency(ExchangeRateDTO exchangeRate, CurrencyEnum currency) {
        return switch (currency) {
            case USD -> exchangeRate.getExchangeRateUSD();
            case CNY -> exchangeRate.getExchangeRateCNY();
            case EUR -> exchangeRate.getExchangeRateEUR();
            case RUB -> BigDecimal.ONE;
        };
    }
}
