package mediasoft.ru.backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Базовая ошибка приложения, от которой наследуются все остальные ошибки. Содержит конструкторы для установки сообщения и статуса ошибки.
 */
@Getter
public class BaseException extends RuntimeException {
    private final String message;
    private HttpStatus status = HttpStatus.BAD_GATEWAY;

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
