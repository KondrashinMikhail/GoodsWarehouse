package mediasoft.ru.backend.models.dto.response.product;

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
public class ProductInOrderResponseDTO {
    private UUID id;
    private String name;
    private BigDecimal quantity;
    private BigDecimal price;
}
