package mediasoft.ru.backend.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidOperationException extends BaseException {
    public InvalidOperationException(String operation) {
        super("Invalid operation '" + operation + "' !", HttpStatus.BAD_REQUEST);
    }
}
