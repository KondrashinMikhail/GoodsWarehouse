package mediasoft.ru.backend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.CurrencyEnum;
import mediasoft.ru.backend.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;
    private String article;
    private String name;
    private String description;
    private ProductCategory category;
    private BigDecimal price;
    private BigDecimal count;
    private LocalDate creationDate;
    private LocalDateTime lastModifiedDate;
    private Boolean isAvailable;
    private CurrencyEnum currency;
}
