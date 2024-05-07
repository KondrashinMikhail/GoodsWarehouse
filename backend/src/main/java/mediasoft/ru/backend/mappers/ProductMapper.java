package mediasoft.ru.backend.mappers;

import mediasoft.ru.backend.dto.ProductDTO;
import mediasoft.ru.backend.dto.request.product.CreateProductRequestDTO;
import mediasoft.ru.backend.dto.request.product.UpdateProductRequestDTO;
import mediasoft.ru.backend.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.dto.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapToModel(ProductDTO productDTO);

    ProductDTO mapToDTO(Product product);

    ProductDTO mapToDTO(CreateProductRequestDTO createProductRequestDTO);

    ProductDTO mapToDTO(UpdateProductRequestDTO updateProductRequestDTO);

    CreateProductResponseDTO mapCreatedModelToResponse(Product product);

    ProductInfoResponseDTO mapModelToInfoResponse(Product product);

    UpdateProductResponseDTO mapToUpdateResponse(Product product);
}
