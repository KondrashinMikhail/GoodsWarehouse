package mediasoft.ru.backend.providers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.CurrencyEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyProvider {
    @Builder.Default
    private CurrencyEnum currency = CurrencyEnum.RUB;
}
