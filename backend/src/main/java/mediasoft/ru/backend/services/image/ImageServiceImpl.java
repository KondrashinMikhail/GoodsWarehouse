package mediasoft.ru.backend.services.image;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.models.entities.Image;
import mediasoft.ru.backend.models.entities.Product;
import mediasoft.ru.backend.repositories.ImageRepository;
import mediasoft.ru.backend.services.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
    private ProductService productService;
    private ImageRepository imageRepository;

    @Override
    public void attachImage(UUID productId, UUID imageId) {
        Product product = productService.getEntityById(productId);
        imageRepository.save(Image.builder()
                .id(imageId)
                .product(product)
                .build());
    }

    @Override
    public String getProductName(UUID productId) {
        Product product = productService.getEntityById(productId);
        return product.getName();
    }

    @Override
    public List<Image> getImages(UUID productId) {
        return imageRepository.findAllByProductId(productId);
    }
}
