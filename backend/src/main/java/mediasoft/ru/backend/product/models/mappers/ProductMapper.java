package mediasoft.ru.backend.product.models.mappers;

import mediasoft.ru.backend.product.models.dto.CreateProductDTO;
import mediasoft.ru.backend.product.models.dto.ProductDTO;
import mediasoft.ru.backend.product.models.dto.UpdateProductDTO;
import mediasoft.ru.backend.product.models.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO mapToDTO(Product product);

    Product mapToModel(CreateProductDTO createProductDTO);

    UpdateProductDTO mapToDTO(ProductDTO productDTO);
}
