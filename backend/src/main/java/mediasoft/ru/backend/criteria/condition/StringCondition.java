package mediasoft.ru.backend.criteria.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.criteria.CriteriaOptions;
import mediasoft.ru.backend.criteria.specification.PredicateSpecification;
import mediasoft.ru.backend.criteria.specification.StringPredicateSpecification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StringCondition implements Condition<String> {
    private String field;
    private String value;
    private CriteriaOptions operation;
    private final PredicateSpecification<String> predicateSpecification = new StringPredicateSpecification();
}
