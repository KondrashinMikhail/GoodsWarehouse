package mediasoft.ru.backend.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDetails> handleUnexpectedException(Exception exception) {
        return ResponseEntity
                .status(BAD_GATEWAY)
                .body(ErrorDetails.builder()
                        .message(exception.getMessage())
                        .exceptionClassFrom(exception.getStackTrace()[0].getClassName())
                        .exception(exception.getClass().getSimpleName())
                        .build());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorDetails> handleBaseException(BaseException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorDetails.builder()
                        .message(exception.getMessage())
                        .exceptionClassFrom(exception.getStackTrace()[0].getClassName())
                        .exception(exception.getClass().getSimpleName())
                        .build());
    }
}

