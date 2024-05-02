package mediasoft.ru.backend.product.currency.services.interfaces;

import net.minidev.json.JSONObject;

public interface CurrencyServiceClient {
    JSONObject getCurrencyExchangeRate();
}
