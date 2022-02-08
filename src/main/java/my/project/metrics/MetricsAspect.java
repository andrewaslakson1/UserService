package my.project.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;

import my.project.exception.exceptions.InvalidMetricsConfigurationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class MetricsAspect {

    private final Counter requestCount;
    private final Counter errorCount;

    private final Counter checkNameAvailabilityRequestCount;
    private final Counter addUserRequestCount;
    private final Counter getUserRequestCount;
    private final Counter updateUserRequestCount;
    private final Counter deleteUserRequestCount;

    private final Histogram checkNameAvailabiltyRequestHistogram;
    private final Histogram addUserRequestHistogram;
    private final Histogram getUserRequestHistogram;
    private final Histogram updateUserRequestHistogram;
    private final Histogram deleteUserRequestHistogram;

    @Autowired
    public MetricsAspect(CollectorRegistry collectorRegistry) {
        requestCount = Counter.build()
                .name("request_count")
                .help("Total number of requests to user service")
                .register(collectorRegistry);

        errorCount = Counter.build()
                .name("error_count")
                .help("Total number of errors from user service")
                .register(collectorRegistry);

        /**********************************************************/

        checkNameAvailabilityRequestCount = Counter.build()
                .name("name_availability_request_count")
                .help("Total number of requests to check name availability")
                .register(collectorRegistry);

        addUserRequestCount = Counter.build()
                .name("add_user_request_count")
                .help("Total number of requests to add user")
                .register(collectorRegistry);

        getUserRequestCount = Counter.build()
                .name("get_user_request_count")
                .help("Total number of requests to get user")
                .register(collectorRegistry);

        updateUserRequestCount = Counter.build()
                .name("update_user_request_count")
                .help("Total number of requests to update user")
                .register(collectorRegistry);

        deleteUserRequestCount = Counter.build()
                .name("delete_user_request_count")
                .help("Total number of requests to delete user")
                .register(collectorRegistry);

        /**********************************************************/

        checkNameAvailabiltyRequestHistogram = Histogram.build()
                .name("name_availability_request_histogram")
                .help("Histogram showing request time for requesting name availability")
                .register(collectorRegistry);

        addUserRequestHistogram = Histogram.build()
                .name("add_user_request_histogram")
                .help("Histogram showing request time for requesting add user")
                .register(collectorRegistry);

        getUserRequestHistogram = Histogram.build()
                .name("get_user_request_histogram")
                .help("Histogram showing request time for requesting get user")
                .register(collectorRegistry);

        updateUserRequestHistogram = Histogram.build()
                .name("update_user_request_histogram")
                .help("Histogram showing request time for requesting update user")
                .register(collectorRegistry);

        deleteUserRequestHistogram = Histogram.build()
                .name("delete_user_request_histogram")
                .help("Histogram showing request time for requesting delete user")
                .register(collectorRegistry);

    }

    @Around("@annotation(my.project.metrics.CollectMetrics)")
    public Object collectMetrics(ProceedingJoinPoint jp) throws Throwable {
        Histogram.Timer timer;

        ControllerEndpoints endPoint =
                ((MethodSignature) jp.getSignature())
                        .getMethod()
                        .getAnnotation(CollectMetrics.class)
                        .endPoint();

        switch (endPoint) {
            case CHECK_USERNAME_AVAILABILITY:
                timer = checkNameAvailabiltyRequestHistogram.startTimer();
                checkNameAvailabilityRequestCount.inc();
                break;

            case ADD_USER:
                timer = addUserRequestHistogram.startTimer();
                addUserRequestCount.inc();
                break;

            case GET_USER:
                timer = getUserRequestHistogram.startTimer();
                getUserRequestCount.inc();
                break;

            case UPDATE_USER:
                timer = updateUserRequestHistogram.startTimer();
                updateUserRequestCount.inc();
                break;

            case DELETE_USER:
                timer = deleteUserRequestHistogram.startTimer();
                deleteUserRequestCount.inc();
                break;

            default:
                throw new InvalidMetricsConfigurationException();

        }

        try {
            return jp.proceed();

        } catch (Throwable t) {
            errorCount.inc();
            throw t;

        } finally {
            timer.observeDuration();

        }

    }

}
