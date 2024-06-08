package mediasoft.ru.backend.models.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.models.dto.response.product.ProductInOrderResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoResponseDTO {
    private UUID orderId;
    private List<ProductInOrderResponseDTO> products;
    private BigDecimal totalPrice;
}
