package mediasoft.ru.backend.web.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddDeliveryDateRequest {
    private UUID orderId;
    private LocalDate deliveryDate;
}
