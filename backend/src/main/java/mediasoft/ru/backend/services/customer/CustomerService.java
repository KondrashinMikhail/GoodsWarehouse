package mediasoft.ru.backend.services.customer;

import mediasoft.ru.backend.models.entities.Customer;

public interface CustomerService {
    Customer getEntityById(Long id);
}
