package mediasoft.ru.backend.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleUnexpectedException(Exception exception) {
        return ResponseEntity.status(BAD_GATEWAY)
                .body(new BaseException(exception.getMessage()).formatResponseData());
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<Object> handleBaseException(BaseException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.formatResponseData());
    }
}

