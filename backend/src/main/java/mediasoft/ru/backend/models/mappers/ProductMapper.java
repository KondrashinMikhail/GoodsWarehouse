package mediasoft.ru.backend.models.mappers;

import mediasoft.ru.backend.models.dto.ProductDTO;
import mediasoft.ru.backend.models.dto.request.product.CreateProductRequestDTO;
import mediasoft.ru.backend.models.dto.request.product.UpdateProductRequestDTO;
import mediasoft.ru.backend.models.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.models.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapToModel(ProductDTO productDTO);

    ProductDTO mapToDTO(Product product);

    ProductDTO mapToDTO(CreateProductRequestDTO createProductRequestDTO);

    ProductDTO mapToDTO(UpdateProductRequestDTO updateProductRequestDTO);

    CreateProductResponseDTO mapCreatedModelToResponse(Product product);

    ProductInfoResponseDTO mapModelToInfoResponse(Product product);

    ProductInfoResponseDTO mapModelToInfoResponse(ProductDTO productDTO);

    UpdateProductResponseDTO mapToUpdateResponse(Product product);
}
