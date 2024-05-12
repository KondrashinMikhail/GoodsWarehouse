package mediasoft.ru.backend.criteria.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.criteria.CriteriaOptions;
import mediasoft.ru.backend.criteria.specification.DoublePredicateSpecification;
import mediasoft.ru.backend.criteria.specification.PredicateSpecification;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoubleCondition implements Condition<Double> {
    private String field;
    private Double value;
    private CriteriaOptions operation;
    private PredicateSpecification<Double> predicateSpecification = new DoublePredicateSpecification();
}
