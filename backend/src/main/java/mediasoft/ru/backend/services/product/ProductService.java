package mediasoft.ru.backend.services.product;

import mediasoft.ru.backend.models.dto.ProductDTO;
import mediasoft.ru.backend.models.dto.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.models.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.models.entities.Product;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDTO createProduct(ProductDTO createProductRequestDTO);

    List<ProductInfoResponseDTO> getAllProducts(Pageable pageable);

    ProductInfoResponseDTO getProductById(UUID id);

    UpdateProductResponseDTO updateProduct(ProductDTO updateProductRequestDTO);

    void deleteProduct(UUID id);

    Product getEntityById(UUID id);

    void checkProductBeforeAddToOrder(List<ProductInOrderRequestDTO> productInOrderRequestDTO);

    void decrementProductCount(UUID productId, BigDecimal count);

    List<ProductInOrderResponseDTO> getProductsInOrder(UUID orderId);

    void returnProduct(UUID productId, BigDecimal count);
}
