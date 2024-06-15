package mediasoft.ru.backend.services.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Primary
@Service
@ConditionalOnProperty(name = "rest.account-service.mock.enabled")
public class AccountServiceClientMockImpl implements AccountServiceClient {
    @Override
    public CompletableFuture<Map<String, String>> getAccountNumbers(List<String> logins) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> map = new HashMap<>();
            logins.forEach(login -> map.put(login, String.format("%012d", new Random().nextInt(1000000000))));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            return map;
        });
    }
}
