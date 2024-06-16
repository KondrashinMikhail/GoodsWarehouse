package mediasoft.ru.backend.persistence.repositories;

import mediasoft.ru.backend.enums.OrderStatus;
import mediasoft.ru.backend.persistence.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    List<Order> findAllByStatusIn(List<OrderStatus> statuses);

    Order findByProcessId(String processId);
}
