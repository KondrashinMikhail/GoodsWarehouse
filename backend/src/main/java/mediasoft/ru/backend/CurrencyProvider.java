package mediasoft.ru.backend;

import lombok.Getter;
import lombok.Setter;
import mediasoft.ru.backend.enums.CurrencyEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Setter
@Getter
public class CurrencyProvider {
    CurrencyEnum currency;
}
