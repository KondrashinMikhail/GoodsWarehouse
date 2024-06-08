package mediasoft.ru.backend.criteria.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.CriteriaOptions;
import mediasoft.ru.backend.criteria.specification.LocalDateTimePredicateSpecification;
import mediasoft.ru.backend.criteria.specification.PredicateSpecification;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalDateTimeCondition implements Condition<LocalDateTime> {
    private String field;
    private LocalDateTime value;
    private CriteriaOptions operation;
    private final PredicateSpecification<LocalDateTime> predicateSpecification = new LocalDateTimePredicateSpecification();
}
