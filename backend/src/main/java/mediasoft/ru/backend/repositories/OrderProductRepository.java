package mediasoft.ru.backend.repositories;

import mediasoft.ru.backend.models.entities.OrderProduct;
import mediasoft.ru.backend.models.entities.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductKey>, JpaSpecificationExecutor<OrderProduct> {
    List<OrderProduct> findAllByOrderId(UUID orderId);
}
