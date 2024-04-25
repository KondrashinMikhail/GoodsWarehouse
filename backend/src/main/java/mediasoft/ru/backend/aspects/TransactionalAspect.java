package mediasoft.ru.backend.aspects;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.UUID;

@Aspect
@Component
@Log4j2
public class TransactionalAspect extends TransactionSynchronizationAdapter {
    private UUID transactionUUID;
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();


    @Before("@annotation(jakarta.transaction.Transactional)")
    public void registerTransactionSynchronization() {
        transactionUUID = UUID.randomUUID();
        log.info("Assigned transaction with UUID - {}", transactionUUID);
        TransactionSynchronizationManager.registerSynchronization(this);
    }


    @Override
    public void beforeCommit(boolean readOnly) {
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        log.info("Started transaction {} commit", transactionUUID);
        startTime.set(System.currentTimeMillis());
    }

    @Override
    public void afterCommit() {
        double executionTime = (System.currentTimeMillis() - startTime.get());
        log.info("Finished transaction {} commit in {} s", transactionUUID, executionTime / 1000.0);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
