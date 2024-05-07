package mediasoft.ru.backend.models.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.ProductCategory;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequestDTO {
    private UUID id;
    private String article;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private BigDecimal count;
}
