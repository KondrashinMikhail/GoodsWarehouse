package mediasoft.ru.backend.services.customer;

import mediasoft.ru.backend.models.entities.Customer;

import java.util.UUID;

public interface CustomerService {
    Customer getEntityById(UUID id);
}
