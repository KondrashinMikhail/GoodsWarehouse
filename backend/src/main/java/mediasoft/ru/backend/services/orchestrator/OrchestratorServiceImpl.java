package mediasoft.ru.backend.services.orchestrator;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.configurations.rest.RestConfiguration;
import mediasoft.ru.backend.web.request.order.CamundaProcessOrderRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class OrchestratorServiceImpl implements OrchestratorService {
    private final RestConfiguration restConfiguration;
    private final WebClient webClientOrchestrator;

    @Override
    public String startProcess(CamundaProcessOrderRequest request) {
        return webClientOrchestrator
                .post()
                .uri(restConfiguration.getOrchestratorService().getMethods().getStartProcess())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
