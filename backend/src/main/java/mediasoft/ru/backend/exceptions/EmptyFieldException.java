package mediasoft.ru.backend.exceptions;

import mediasoft.ru.exceptionsstarter.BaseException;
import org.springframework.http.HttpStatus;

public class EmptyFieldException extends BaseException {
    public EmptyFieldException() {
        super("All fields should have been filled!", HttpStatus.BAD_REQUEST);
    }
}
