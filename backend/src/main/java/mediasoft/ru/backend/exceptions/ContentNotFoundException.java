package mediasoft.ru.backend.exceptions;

import org.springframework.http.HttpStatus;

public class ContentNotFoundException extends BaseException {
    public ContentNotFoundException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
