package mediasoft.ru.backend.exceptions;

import mediasoft.ru.exceptionsstarter.BaseException;
import org.springframework.http.HttpStatus;

public class UniqueFieldException extends BaseException {
    public UniqueFieldException(String uniqueField) {
        super(String.format("Entity with such '%s' value already exists!", uniqueField), HttpStatus.BAD_REQUEST);
    }
}
