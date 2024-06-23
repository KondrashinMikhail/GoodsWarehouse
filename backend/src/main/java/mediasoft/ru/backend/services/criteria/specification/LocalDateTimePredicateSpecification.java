package mediasoft.ru.backend.services.criteria.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;

public class LocalDateTimePredicateSpecification implements PredicateSpecification<LocalDateTime> {
    @Override
    public Predicate getEqualsPredicate(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(criteriaBuilder.function("date", LocalDateTime.class, expression), value.toLocalDate());
    }

    @Override
    public Predicate getNotEqualsPredicate(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.notEqual(expression, value);
    }

    @Override
    public Predicate getLikePredicate(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.between(expression, value.minusDays(7), value.plusDays(7));
    }

    @Override
    public Predicate getGreaterThanPredicate(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThan(expression, value);
    }

    @Override
    public Predicate getGreaterThanOrEqualPredicate(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getLessThanPredicate(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThan(expression, value);
    }

    @Override
    public Predicate getLessThanOrEqualPredicate(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lessThanOrEqualTo(expression, value);
    }
}
