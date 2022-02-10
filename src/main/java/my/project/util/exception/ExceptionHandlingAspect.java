package my.project.util.exception;

import my.project.util.exception.exceptions.DuplicateUsernameException;
import my.project.util.exception.exceptions.InvalidMetricsConfigurationException;
import my.project.util.exception.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingAspect {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
            DuplicateUsernameException.class
    })
    public ExceptionResponse usernameConflictHandler(Exception e) {
        return new ExceptionResponse(409, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            UserNotFoundException.class
    })
    public ExceptionResponse userNotFoundHandler(Exception e) {
        return new ExceptionResponse(404, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            InvalidMetricsConfigurationException.class
    })
    public ExceptionResponse serverErrorHandler(Exception e) {
        return new ExceptionResponse(500, e);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class
    })
    public ExceptionResponse httpRequestMethodNotSupportedExceptionHandler(Exception e) {
        return new ExceptionResponse(405, e);
    }
}
