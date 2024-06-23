package mediasoft.ru.backend.services.criteria.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;


public interface PredicateSpecification<T> {
    Predicate getEqualsPredicate(Expression<T> expression, T value, CriteriaBuilder criteriaBuilder);

    Predicate getNotEqualsPredicate(Expression<T> expression, T value, CriteriaBuilder criteriaBuilder);

    Predicate getLikePredicate(Expression<T> expression, T value, CriteriaBuilder criteriaBuilder);

    Predicate getGreaterThanPredicate(Expression<T> expression, T value, CriteriaBuilder criteriaBuilder);

    Predicate getGreaterThanOrEqualPredicate(Expression<T> expression, T value, CriteriaBuilder criteriaBuilder);

    Predicate getLessThanPredicate(Expression<T> expression, T value, CriteriaBuilder criteriaBuilder);

    Predicate getLessThanOrEqualPredicate(Expression<T> expression, T value, CriteriaBuilder criteriaBuilder);
}
