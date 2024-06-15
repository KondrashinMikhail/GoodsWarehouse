package mediasoft.ru.backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeRateDTO {
    private BigDecimal exchangeRateCNY;
    private BigDecimal exchangeRateUSD;
    private BigDecimal exchangeRateEUR;
}
