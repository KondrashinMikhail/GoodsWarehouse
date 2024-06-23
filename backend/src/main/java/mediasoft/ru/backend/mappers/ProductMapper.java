package mediasoft.ru.backend.mappers;

import mediasoft.ru.backend.models.ProductDTO;
import mediasoft.ru.backend.web.request.product.CreateProductRequestDTO;
import mediasoft.ru.backend.web.request.product.UpdateProductRequestDTO;
import mediasoft.ru.backend.web.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.web.response.product.ProductInOrderResponseDTO;
import mediasoft.ru.backend.web.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.web.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.persistence.entities.Product;
import mediasoft.ru.backend.persistence.projections.ProductInOrderProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapToModel(ProductDTO productDTO);

    ProductDTO mapToDTO(CreateProductRequestDTO createProductRequestDTO);

    ProductDTO mapToDTO(UpdateProductRequestDTO updateProductRequestDTO);

    CreateProductResponseDTO mapModelToResponse(Product product);

    ProductInfoResponseDTO mapModelToInfoResponse(Product product);

    UpdateProductResponseDTO mapModelToUpdateResponse(Product product);

    ProductInOrderResponseDTO mapProjectionToResponse(ProductInOrderProjection productProjection);
}
