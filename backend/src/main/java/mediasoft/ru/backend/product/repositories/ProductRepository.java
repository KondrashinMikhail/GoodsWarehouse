package mediasoft.ru.backend.product.repositories;

import mediasoft.ru.backend.product.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByArticle(String article);

    @Query(value = "select * from product order by id limit :limit offset :offset", nativeQuery = true)
    ArrayList<Product> findAllWithLimit(int limit, int offset);
}
