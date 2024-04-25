package mediasoft.ru.backend.aspects;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class TimeMeasuringAspect {
    @Around("@annotation(mediasoft.ru.backend.annotations.TimeMeasuring)")
    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("----------------------------------------------------------------------------------");
        log.info("Started '{}.{}'",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("Finished '{}.{}' in {} s",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime / 1000.0);
        log.info("----------------------------------------------------------------------------------");
        return proceed;
    }
}
