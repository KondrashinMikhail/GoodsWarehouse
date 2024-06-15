package mediasoft.ru.backend.services.account;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface AccountServiceClient {
    CompletableFuture<Map<String, String>> getAccountNumbers(List<String> logins);
}
