package mediasoft.ru.backend.web.request.order;

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
public class CamundaProcessOrderRequest {
    private UUID orderId;
    private UUID customerId;
    private String deliveryAddress;
    private String inn;
    private String accountNumber;
    private BigDecimal totalPrice;
}
