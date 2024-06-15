package mediasoft.ru.backend.services.customer;

import mediasoft.ru.backend.persistence.entities.Customer;

import java.util.UUID;

public interface CustomerService {
    Customer getEntityById(UUID id);
}
