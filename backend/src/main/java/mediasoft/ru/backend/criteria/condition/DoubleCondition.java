package mediasoft.ru.backend.criteria.condition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.CriteriaOptions;
import mediasoft.ru.backend.criteria.specification.DoublePredicateSpecification;
import mediasoft.ru.backend.criteria.specification.PredicateSpecification;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoubleCondition implements Condition<BigDecimal> {
    private String field;
    private BigDecimal value;
    private CriteriaOptions operation;
    private PredicateSpecification<BigDecimal> predicateSpecification = new DoublePredicateSpecification();
}
