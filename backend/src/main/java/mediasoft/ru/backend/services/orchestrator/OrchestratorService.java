package mediasoft.ru.backend.services.orchestrator;

import mediasoft.ru.backend.web.request.order.CamundaProcessOrderRequest;

public interface OrchestratorService {
    String startProcess(CamundaProcessOrderRequest request);
}
