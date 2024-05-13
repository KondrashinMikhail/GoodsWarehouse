package mediasoft.ru.backend.services.currency;

import mediasoft.ru.backend.models.dto.ExchangeRateDTO;

public interface CurrencyService {
    ExchangeRateDTO getCurrencyExchangeRate();
}
