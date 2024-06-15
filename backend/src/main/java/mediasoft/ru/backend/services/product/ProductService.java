package mediasoft.ru.backend.services.product;

import mediasoft.ru.backend.services.criteria.condition.Condition;
import mediasoft.ru.backend.models.ProductDTO;
import mediasoft.ru.backend.web.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.web.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.web.response.product.ProductInOrderResponseDTO;
import mediasoft.ru.backend.web.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.web.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.persistence.entities.Product;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {
    CreateProductResponseDTO createProduct(ProductDTO createProductRequestDTO);

    List<ProductInfoResponseDTO> getAllProducts(Pageable pageable);

    ProductInfoResponseDTO getProductById(UUID id);

    UpdateProductResponseDTO updateProduct(ProductDTO updateProductRequestDTO);

    void deleteProduct(UUID id);

    Product getEntityById(UUID id);

    Map<UUID, Product> getMapOfProducts(List<UUID> productIds);

    void checkProductBeforeAddToOrder(List<ProductInOrderRequestDTO> productInOrderRequestDTO, Map<UUID, Product> products);

    void decrementProductCount(Product product, BigDecimal count);

    List<ProductInOrderResponseDTO> getProductsInOrder(UUID orderId);

    void returnProduct(Product product, BigDecimal count);

    List<ProductInfoResponseDTO> searchProducts(Pageable pageable, List<Condition<?>> conditions);
}
