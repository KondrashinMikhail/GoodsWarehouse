package mediasoft.ru.backend.web.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.web.request.product.ProductInOrderRequestDTO;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDTO {
    private String deliveryAddress;
    private List<ProductInOrderRequestDTO> products;
}
