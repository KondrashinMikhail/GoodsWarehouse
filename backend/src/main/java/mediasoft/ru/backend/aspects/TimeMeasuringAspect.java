package mediasoft.ru.backend.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TimeMeasuringAspect {
    @Around("@annotation(mediasoft.ru.backend.annotations.TimeMeasuring)")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Started '{}.{}'",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Exception ex) {
            log.info("Finished with exception {} in '{}.{}' in {} s",
                    ex.getMessage(),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ((System.currentTimeMillis() - start) / 1000.0));
            return null;
        }
        long executionTime = System.currentTimeMillis() - start;
        log.info("Finished '{}.{}' in {} s",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime / 1000.0);
        return proceed;
    }
}
