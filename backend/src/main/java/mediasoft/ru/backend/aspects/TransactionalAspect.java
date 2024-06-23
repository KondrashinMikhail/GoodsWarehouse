package mediasoft.ru.backend.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Aspect
@Component
@Slf4j
public class TransactionalAspect extends TransactionSynchronizationAdapter {
    private UUID transactionUUID;
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Before("@annotation(jakarta.transaction.Transactional)")
    public void registerTransactionSynchronization() {
        transactionUUID = UUID.randomUUID();
        log.info("Assigned transaction with UUID - {}", transactionUUID);
        startTime.set(System.currentTimeMillis());
        TransactionSynchronizationManager.registerSynchronization(this);
    }

    @Override
    public void afterCommit() {
        double executionTime = (System.currentTimeMillis() - startTime.get());
        log.info("Finished transaction {} commit in {} s", transactionUUID, executionTime / 1000.0);
    }
}
