package mediasoft.ru.backend.exceptions;

import org.springframework.http.HttpStatus;

public class NullableValueException extends BaseException {
    public NullableValueException() {
        super("Value should not be null!", HttpStatus.BAD_REQUEST);
    }
}
