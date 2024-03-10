package mediasoft.ru.backend.product.models.dto;

import lombok.*;
import mediasoft.ru.backend.product.models.entities.ProductCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;
    private String article;
    private String name;
    private String description;
    private ProductCategory category;
    private Double price;
    private Integer count;
    private LocalDate creationDate;
    private LocalDateTime lastModifiedDate;
}
