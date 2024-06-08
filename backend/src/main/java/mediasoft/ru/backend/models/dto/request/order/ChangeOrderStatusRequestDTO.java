package mediasoft.ru.backend.models.dto.request.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mediasoft.ru.backend.enums.OrderStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderStatusRequestDTO {
    private OrderStatus status;
}
