package mediasoft.ru.backend.criteria.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;


public class DoublePredicateSpecification implements PredicateSpecification<Double> {
    @Override
    public Predicate getEqualsPredicate(Expression<Double> expression, Double value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(expression, value);
    }

    @Override
    public Predicate getNotEqualsPredicate(Expression<Double> expression, Double value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.notEqual(expression, value);
    }

    @Override
    public Predicate getLikePredicate(Expression<Double> expression, Double value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.between(expression, value * 0.9, value * 1.1);
    }

    @Override
    public Predicate getGreaterThanPredicate(Expression<Double> expression, Double value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(expression, value);
    }

    @Override
    public Predicate getGreaterThanOrEqualPredicate(Expression<Double> expression, Double value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getLessThanPredicate(Expression<Double> expression, Double value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThan(expression, value);
    }

    @Override
    public Predicate getLessThanOrEqualPredicate(Expression<Double> expression, Double value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThanOrEqualTo(expression, value);
    }
}
