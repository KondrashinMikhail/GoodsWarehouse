package mediasoft.ru.backend.models.mappers;

import mediasoft.ru.backend.models.dto.ProductDTO;
import mediasoft.ru.backend.models.dto.request.product.CreateProductRequestDTO;
import mediasoft.ru.backend.models.dto.request.product.UpdateProductRequestDTO;
import mediasoft.ru.backend.models.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.models.entities.Product;
import mediasoft.ru.backend.models.projections.ProductInOrderProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapToModel(ProductDTO productDTO);

    ProductDTO mapToDTO(Product product);

    ProductDTO mapToDTO(CreateProductRequestDTO createProductRequestDTO);

    ProductDTO mapToDTO(UpdateProductRequestDTO updateProductRequestDTO);

    CreateProductResponseDTO mapModelToResponse(Product product);

    ProductInfoResponseDTO mapModelToInfoResponse(Product product);

    UpdateProductResponseDTO mapModelToUpdateResponse(Product product);

    ProductInOrderResponseDTO mapProjectionToResponse(ProductInOrderProjection productProjection);
}
