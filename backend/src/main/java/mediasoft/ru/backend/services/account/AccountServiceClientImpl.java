package mediasoft.ru.backend.services.account;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.configurations.rest.RestConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class AccountServiceClientImpl implements AccountServiceClient {
    private final RestConfiguration restConfiguration;
    private final WebClient webClientAccount;

    @Override
    public CompletableFuture<Map<String, String>> getAccountNumbers(List<String> logins) {
        return webClientAccount
                .post()
                .uri(restConfiguration.getAccountService().getMethods().getGetAccountNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .retry(restConfiguration.getAccountService().getRetryCount())
                .toFuture();
    }
}
