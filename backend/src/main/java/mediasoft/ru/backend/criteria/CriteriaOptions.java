package mediasoft.ru.backend.criteria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CriteriaOptions {
    EQUALS("==", "equal"),
    NOT_EQUALS("!=", "notEqual"),
    LESS_THAN("<", "lessThan"),
    GREATER_THAN(">", "greaterThan"),
    LESS_OR_EQUALS("<=", "lessThanOrEqualTo"),
    GREATER_OR_EQUALS(">=", "greaterThanOrEqualTo"),
    LIKE("~", "like");

    private final String operation;
    private final String methodName;

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
