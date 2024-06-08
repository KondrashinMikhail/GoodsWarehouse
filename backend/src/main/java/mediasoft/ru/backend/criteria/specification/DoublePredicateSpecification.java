package mediasoft.ru.backend.criteria.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;


public class DoublePredicateSpecification implements PredicateSpecification<BigDecimal> {
    @Override
    public Predicate getEqualsPredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(expression, value);
    }

    @Override
    public Predicate getNotEqualsPredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.notEqual(expression, value);
    }

    @Override
    public Predicate getLikePredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.between(expression, value.multiply(BigDecimal.valueOf(0.9)), value.multiply(BigDecimal.valueOf(1.1)));
    }

    @Override
    public Predicate getGreaterThanPredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(expression, value);
    }

    @Override
    public Predicate getGreaterThanOrEqualPredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getLessThanPredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThan(expression, value);
    }

    @Override
    public Predicate getLessThanOrEqualPredicate(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThanOrEqualTo(expression, value);
    }
}
