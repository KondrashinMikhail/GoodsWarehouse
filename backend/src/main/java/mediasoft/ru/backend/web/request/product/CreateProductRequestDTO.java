package mediasoft.ru.backend.web.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.ProductCategory;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequestDTO {
    private String article;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private BigDecimal count;
}
