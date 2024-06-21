package mediasoft.ru.backend.repositories;

import mediasoft.ru.backend.models.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID>, JpaSpecificationExecutor<Image> {
    List<Image> findAllByProductId(UUID productId);
}
