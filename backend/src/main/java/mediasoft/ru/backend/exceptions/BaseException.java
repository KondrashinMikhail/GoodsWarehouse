package mediasoft.ru.backend.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    private String message;
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

    public Map<String, Object> formatResponseData() {
        HashMap<String, Object> resData = new HashMap<>();
        resData.put("message", this.message);
        resData.put("status", this.status.value());
        return resData;
    }
}
