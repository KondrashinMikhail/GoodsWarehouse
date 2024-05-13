package mediasoft.ru.backend.repositories;

import mediasoft.ru.backend.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    @Query("""
                 select o from Order o
                 left join o.orderProducts op
                 left join op.product p
                 where o.id = :orderId
            """)
    Optional<Order> findByIdFetchProducts(UUID orderId);
}
