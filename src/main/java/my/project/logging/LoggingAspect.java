package my.project.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger("my.project.logger.aspect");

    @Pointcut("within(my.project..*)")
    public void logAll(){}

    @Before("logAll()")
    public void logMethodStart(JoinPoint jp) {
        String signature = extractMethodSignature(jp);
        String args = Arrays.toString(jp.getArgs());

        logger.debug("{} invoked with provided arguments: {}",
                signature, args);
    }

    @AfterReturning(pointcut = "logAll()", returning = "returned")
    public void logMethodReturn(JoinPoint jp, Object returned) {
        String signature = extractMethodSignature(jp);

        logger.debug("{} successfully returned with value: {}",
                signature, returned);
    }

    @AfterThrowing(pointcut = "logAll()", throwing = "t")
    public void logExceptionThrown(JoinPoint jp, Throwable t) {
        String signature = extractMethodSignature(jp);
        String exceptionName = t.getClass().getName();

        logger.warn("{} was thrown from {} with message: {}",
                exceptionName, signature, t.getMessage());
    }

    private String extractMethodSignature(JoinPoint jp) {
        return jp.getTarget().getClass().toString() + "#" + jp.getSignature().getName();
    }
}
