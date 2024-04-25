package mediasoft.ru.backend.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
