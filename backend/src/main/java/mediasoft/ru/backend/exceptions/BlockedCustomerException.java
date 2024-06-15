package mediasoft.ru.backend.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class BlockedCustomerException extends BaseException {
    public BlockedCustomerException(UUID id) {
        super(String.format("Customer with id - %s is blocked!", id), HttpStatus.FORBIDDEN);
    }
}
