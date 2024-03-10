package mediasoft.ru.backend.product.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mediasoft.ru.backend.product.models.entities.ProductCategory;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDTO {
    private UUID id;
    private String article;
    private String name;
    private String description;
    private ProductCategory category;
    private Double price;
    private Integer count;
}
