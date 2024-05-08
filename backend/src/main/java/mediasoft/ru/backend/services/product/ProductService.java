package mediasoft.ru.backend.services.product;

import mediasoft.ru.backend.models.dto.ProductDTO;
import mediasoft.ru.backend.models.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.UpdateProductResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDTO createProduct(ProductDTO createProductRequestDTO);

    List<ProductInfoResponseDTO> getAllProducts(Pageable pageable);

    ProductInfoResponseDTO getProductById(UUID id);

    UpdateProductResponseDTO updateProduct(ProductDTO updateProductRequestDTO);

    void deleteProduct(UUID id);
}
