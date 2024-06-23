package mediasoft.ru.backend.web.response.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo {
    private UUID id;
    private String accountNumber;
    private String email;
    private String inn;
}
