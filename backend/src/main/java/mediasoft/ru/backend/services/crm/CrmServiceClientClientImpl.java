package mediasoft.ru.backend.services.crm;

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
public class CrmServiceClientClientImpl implements CrmServiceClient {
    private final RestConfiguration restConfiguration;
    private WebClient webClientCrm;

    @Override
    public CompletableFuture<Map<String, String>> getCrms(List<String> logins) {
        return webClientCrm
                .post()
                .uri(restConfiguration.getCrmService().getMethods().getGetInn())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .retry(restConfiguration.getCrmService().getRetryCount())
                .toFuture();
    }
}
