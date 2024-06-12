package mediasoft.ru.backend.services.image;

import mediasoft.ru.backend.models.entities.Image;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    void attachImage(UUID productId, String path);

    String getProductName(UUID productId);

    List<Image> getImages(UUID productId);
}
