package mediasoft.ru.backend.services.customer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.persistence.entities.Customer;
import mediasoft.ru.backend.persistence.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;

    @Override
    public Customer getEntityById(UUID id) {
        return customerRepository.findById(id).orElseThrow(() ->
                new ContentNotFoundException(String.format("Customer with id - %s not found!", id)));
    }
}
