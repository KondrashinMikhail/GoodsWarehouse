package mediasoft.ru.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyEnum {
    RUB(""),
    CNY("CNY"),
    USD("USD"),
    EUR("EUR");

    private String exchangeRateField;
}
