package mediasoft.ru.backend.services.currency;

import mediasoft.ru.backend.models.ExchangeRateDTO;

public interface CurrencyService {
    ExchangeRateDTO getCurrencyExchangeRate();
}
