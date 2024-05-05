package mediasoft.ru.backend.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CriteriaOptions {
    EQUALS("=="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_OR_EQUALS("<="),
    GREATER_OR_EQUALS(">="),
    LIKE("~");

    private final String operation;

    @JsonValue
    public String getOperation() {
        return operation;
    }

    @JsonCreator
    public static CriteriaOptions forValue(String operation) {
        return Arrays.stream(CriteriaOptions.values())
                .filter(option -> option.operation.equals(operation) || option.name().equals(operation))
                .findFirst()
                .orElse(null);
    }
}
