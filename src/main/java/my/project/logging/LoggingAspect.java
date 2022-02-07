package my.project.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LogManager.getLogger();

    @Pointcut("within(my.project..*)")
    public void logAll(){}

    @Before("logAll()")
    public void logMethodStart(JoinPoint jp) {
        String signature = extractMethodSignature(jp);
        String args = Arrays.toString(jp.getArgs());

        logger.info("{} invoked at {} with provided arguments: {}",
                signature, LocalDateTime.now(), args);
    }

    @AfterReturning(pointcut = "logAll()", returning = "returned")
    public void logMethodReturn(JoinPoint jp, Object returned) {
        String signature = extractMethodSignature(jp);

        logger.info("{} successfully returned at {} with value: {}",
                signature, LocalDateTime.now(), returned);
    }

    @AfterThrowing(pointcut = "logAll()", throwing = "t")
    public void logExceptionThrown(JoinPoint jp, Throwable t) {
        String signature = extractMethodSignature(jp);
        String exceptionName = t.getClass().getName();

        logger.warn("{} was thrown from {} at time {}, with message: {}",
                exceptionName, signature, LocalDateTime.now(), t.getMessage());
    }

    private String extractMethodSignature(JoinPoint jp) {
        return jp.getTarget().getClass().toString() + "#" + jp.getSignature().getName();
    }
}
