package mediasoft.ru.backend.exceptions;

import org.springframework.http.HttpStatus;

public class UniqueFieldException extends BaseException {
    public UniqueFieldException(String uniqueField) {
        super(String.format("Entity with such '%s' value already exists!", uniqueField), HttpStatus.BAD_REQUEST);
    }
}
