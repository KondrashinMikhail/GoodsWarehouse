package mediasoft.ru.backend.services.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.models.entities.Customer;
import mediasoft.ru.backend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    @Override
    public Customer getEntityById(Long id) {
        return customerRepository.findById(id).orElseThrow(() ->
                new ContentNotFoundException(String.format("Customer with id - %s not found!", id)));
    }
}
