package mediasoft.ru.backend.criteria;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Condition {
    private String field;
    private Object value;
    private String operation;
}
