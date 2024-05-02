package mediasoft.ru.backend;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mediasoft.ru.backend.product.models.entities.CurrencyEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class HeaderCaptureFilter extends OncePerRequestFilter {
    @Value("${headers.keys.currency}")
    private String CURRENCY_HEADER_KEY;

    private final CurrencyProvider currencyProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerValue = request.getHeader(CURRENCY_HEADER_KEY);

        if (headerValue != null)
            currencyProvider.setCurrency(CurrencyEnum.valueOf(headerValue));
        if (currencyProvider.getCurrency() == null)
            currencyProvider.setCurrency(CurrencyEnum.RUB);

        filterChain.doFilter(request, response);
    }
}
