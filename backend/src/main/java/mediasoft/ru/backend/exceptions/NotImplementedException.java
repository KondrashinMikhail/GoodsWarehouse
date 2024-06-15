package mediasoft.ru.backend.exceptions;

import mediasoft.ru.exceptionsstarter.BaseException;
import org.springframework.http.HttpStatus;

public class NotImplementedException extends BaseException {
    public NotImplementedException(String message) {
        super(message, HttpStatus.NOT_IMPLEMENTED);
    }
}
