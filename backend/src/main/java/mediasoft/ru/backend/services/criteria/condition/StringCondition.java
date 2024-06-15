package mediasoft.ru.backend.services.criteria.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.CriteriaOptions;
import mediasoft.ru.backend.services.criteria.specification.PredicateSpecification;
import mediasoft.ru.backend.services.criteria.specification.StringPredicateSpecification;

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
