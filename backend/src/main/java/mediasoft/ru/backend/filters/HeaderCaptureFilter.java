package mediasoft.ru.backend.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mediasoft.ru.backend.providers.CurrencyProvider;
import mediasoft.ru.backend.configurations.HeadersConfiguration;
import mediasoft.ru.backend.enums.CurrencyEnum;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class HeaderCaptureFilter extends OncePerRequestFilter {
    private HeadersConfiguration headersConfiguration;
    private CurrencyProvider currencyProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String headerValue = request.getHeader(headersConfiguration.getCurrency());

        Optional.ofNullable(headerValue)
                .map(CurrencyEnum::valueOf)
                .ifPresent(currencyProvider::setCurrency);

        filterChain.doFilter(request, response);
    }
}
