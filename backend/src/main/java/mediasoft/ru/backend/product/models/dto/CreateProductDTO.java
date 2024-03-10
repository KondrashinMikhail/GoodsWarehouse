package mediasoft.ru.backend.product.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediasoft.ru.backend.product.models.entities.ProductCategory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {
    private String article;
    private String name;
    private String description;
    private ProductCategory category;
    private Double price;
    private Integer count;
}
