package mediasoft.ru.backend.services.criteria.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class StringPredicateSpecification implements PredicateSpecification<String> {
    @Override
    public Predicate getEqualsPredicate(Expression<String> expression, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(expression, value);
    }

    @Override
    public Predicate getNotEqualsPredicate(Expression<String> expression, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.notEqual(expression, value);
    }

    @Override
    public Predicate getLikePredicate(Expression<String> expression, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(expression, "%" + value + "%");
    }

    @Override
    public Predicate getGreaterThanPredicate(Expression<String> expression, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(expression, value);
    }

    @Override
    public Predicate getGreaterThanOrEqualPredicate(Expression<String> expression, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getLessThanPredicate(Expression<String> expression, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThan(expression, value);
    }

    @Override
    public Predicate getLessThanOrEqualPredicate(Expression<String> expression, String value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThanOrEqualTo(expression, value);
    }
}
