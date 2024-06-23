package mediasoft.ru.backend.services.crm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CrmServiceClient {
    CompletableFuture<Map<String, String>> getCrms(List<String> logins);
}
