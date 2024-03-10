package mediasoft.ru.backend.product.services.interfaces;

import mediasoft.ru.backend.product.models.dto.CreateProductDTO;
import mediasoft.ru.backend.product.models.dto.ProductDTO;
import mediasoft.ru.backend.product.models.dto.UpdateProductDTO;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductDTO createProduct(CreateProductDTO createProductDTO);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(UUID id);

    ProductDTO updateProduct(UpdateProductDTO updateProductDTO);

    ProductDTO deleteProduct(UUID id);
}
