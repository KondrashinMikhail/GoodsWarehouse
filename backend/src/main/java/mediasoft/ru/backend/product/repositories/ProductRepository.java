package mediasoft.ru.backend.product.repositories;

import jakarta.persistence.LockModeType;
import mediasoft.ru.backend.product.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByArticle(String article);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p order by p.id limit :limit offset :offset")
    ArrayList<Product> findAllWithLimit(@Param("limit") int limit, @Param("offset") int offset);
}
