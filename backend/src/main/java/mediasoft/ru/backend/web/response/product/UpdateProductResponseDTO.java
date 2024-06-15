package mediasoft.ru.backend.web.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductResponseDTO {
    private UUID id;
    private String article;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private BigDecimal count;
    private LocalDateTime lastModifiedDate;
}

