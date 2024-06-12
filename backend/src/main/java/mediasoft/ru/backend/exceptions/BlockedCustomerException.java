package mediasoft.ru.backend.exceptions;

import mediasoft.ru.exceptionsstarter.BaseException;
import org.springframework.http.HttpStatus;

public class BlockedCustomerException extends BaseException {
    public BlockedCustomerException(Long id) {
        super(String.format("Customer with id - %s is blocked!", id), HttpStatus.FORBIDDEN);
    }
}
