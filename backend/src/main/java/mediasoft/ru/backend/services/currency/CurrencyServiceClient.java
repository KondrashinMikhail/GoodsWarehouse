package mediasoft.ru.backend.services.currency;

import net.minidev.json.JSONObject;

public interface CurrencyServiceClient {
    JSONObject getCurrencyExchangeRate();
}
