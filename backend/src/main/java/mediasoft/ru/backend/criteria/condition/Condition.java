package mediasoft.ru.backend.criteria.condition;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import mediasoft.ru.backend.enums.CriteriaOptions;
import mediasoft.ru.backend.criteria.specification.PredicateSpecification;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, property = "field")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringCondition.class, name = "name"),
        @JsonSubTypes.Type(value = StringCondition.class, name = "description"),
        @JsonSubTypes.Type(value = LocalDateTimeCondition.class, name = "createdDate"),
        @JsonSubTypes.Type(value = DoubleCondition.class, name = "price")
})
public interface Condition<T> {
    String getField();

    T getValue();

    CriteriaOptions getOperation();

    PredicateSpecification<T> getPredicateSpecification();
}
