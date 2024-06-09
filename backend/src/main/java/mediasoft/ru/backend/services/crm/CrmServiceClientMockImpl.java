package mediasoft.ru.backend.services.crm;

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
@ConditionalOnProperty(name = "rest.crm-service.mock.enabled")
public class CrmServiceClientMockImpl implements CrmServiceClient {
    @Override
    public CompletableFuture<Map<String, String>> getCrms(List<String> logins) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> map = new HashMap<>();
            logins.forEach(login -> map.put(login, generateRandomInn()));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            return map;
        });
    }

    private String generateRandomInn() {
        Random random = new Random();
        StringBuilder innBuilder = new StringBuilder();

        for (int i = 0; i < 11; i++) innBuilder.append(random.nextInt(10));

        int checkDigit = calculateCheckDigit(innBuilder.toString());
        innBuilder.append(checkDigit);

        return innBuilder.toString();
    }

    private int calculateCheckDigit(String innWithoutCheckDigit) {
        int sum = 0;
        for (int i = 0; i < innWithoutCheckDigit.length(); i++) {
            sum += Integer.parseInt(String.valueOf(innWithoutCheckDigit.charAt(i)));
        }
        return (sum % 11) % 10;
    }
}
