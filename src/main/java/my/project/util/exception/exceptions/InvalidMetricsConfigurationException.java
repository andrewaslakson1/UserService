package my.project.exception.exceptions;

public class InvalidMetricsConfigurationException extends RuntimeException {
    public InvalidMetricsConfigurationException() {
        super("endpoint is not labeled properly for measuring metrics");
    }

    public InvalidMetricsConfigurationException(String msg) {
        super(msg);
    }
}
