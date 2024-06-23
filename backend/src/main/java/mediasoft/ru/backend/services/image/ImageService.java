package mediasoft.ru.backend.services.image;

import mediasoft.ru.backend.persistence.entities.Image;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    void attachImage(UUID productId, UUID imageId);

    String getProductName(UUID productId);

    List<Image> getImages(UUID productId);
}
