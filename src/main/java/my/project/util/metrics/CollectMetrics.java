package my.project.metrics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CollectMetrics {
    ControllerEndpoints endPoint() default ControllerEndpoints.UNASSIGNED;

}
