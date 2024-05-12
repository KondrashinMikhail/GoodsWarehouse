package mediasoft.ru.backend.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidFieldException extends BaseException {
    public InvalidFieldException(String fieldName) {
        super("Invalid field '" + fieldName + "' !", HttpStatus.BAD_REQUEST);
    }
}
