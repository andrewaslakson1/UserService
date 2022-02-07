package my.project.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExceptionResponse {

    private int statusCode;
    private Throwable exception;
    private String message;
    private LocalDateTime time;

    public ExceptionResponse(int statusCode, Throwable exception) {
        this.statusCode = statusCode;
        this.exception = exception;
        this.message = exception.getMessage();
        this.time = LocalDateTime.now();
    }

    public ExceptionResponse(int statusCode, Throwable exception, String message) {
        this.statusCode = statusCode;
        this.exception = exception;
        this.message = message;
        this.time = LocalDateTime.now();
    }
}
