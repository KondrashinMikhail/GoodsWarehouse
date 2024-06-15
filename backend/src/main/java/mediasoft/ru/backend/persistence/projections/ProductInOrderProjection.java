package mediasoft.ru.backend.persistence.projections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInOrderProjection {
    private UUID id;
    private String name;
    private BigDecimal quantity;
    private BigDecimal price;
}
