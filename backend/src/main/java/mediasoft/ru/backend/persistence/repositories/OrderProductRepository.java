package mediasoft.ru.backend.persistence.repositories;

import mediasoft.ru.backend.persistence.entities.OrderProduct;
import mediasoft.ru.backend.persistence.entities.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey>, JpaSpecificationExecutor<OrderProduct> {
}
