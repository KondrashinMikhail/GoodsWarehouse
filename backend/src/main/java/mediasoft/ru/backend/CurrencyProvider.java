package mediasoft.ru.backend;

import lombok.Getter;
import lombok.Setter;
import mediasoft.ru.backend.product.models.entities.CurrencyEnum;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Setter
@Getter
public class CurrencyProvider {
    CurrencyEnum currency;
}
