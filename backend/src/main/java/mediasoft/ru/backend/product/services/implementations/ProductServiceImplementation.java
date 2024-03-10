package mediasoft.ru.backend.product.services.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.product.models.dto.CreateProductDTO;
import mediasoft.ru.backend.product.models.dto.ProductDTO;
import mediasoft.ru.backend.product.models.dto.UpdateProductDTO;
import mediasoft.ru.backend.product.models.entities.Product;
import mediasoft.ru.backend.product.models.mappers.ProductMapper;
import mediasoft.ru.backend.product.repositories.ProductRepository;
import mediasoft.ru.backend.product.services.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@AllArgsConstructor
public class ProductServiceImplementation implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO createProduct(CreateProductDTO createProductDTO) {
        Product product = productMapper.mapToModel(createProductDTO);
        product.setCreationDate(LocalDate.now());
        Product savedProduct = productRepository.save(product);
        log.info(String.format("Saved new product with id - %s", savedProduct.getId()));
        return productMapper.mapToDTO(savedProduct);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream().map(productMapper::mapToDTO).toList();
    }

    @Override
    public ProductDTO getProductById(UUID id) {
        Product product = getEntityById(id);
        return productMapper.mapToDTO(product);
    }

    @Override
    public ProductDTO updateProduct(UpdateProductDTO updateProductDTO) {
        Product sourceProduct = getEntityById(updateProductDTO.getId());

        sourceProduct.setArticle(updateProductDTO.getArticle());
        sourceProduct.setName(updateProductDTO.getName());
        sourceProduct.setDescription(updateProductDTO.getDescription());
        sourceProduct.setCategory(updateProductDTO.getCategory());
        sourceProduct.setPrice(updateProductDTO.getPrice());
        sourceProduct.setCount(updateProductDTO.getCount());

        Product updatedProduct = productRepository.save(sourceProduct);
        return productMapper.mapToDTO(updatedProduct);
    }

    @Override
    public ProductDTO deleteProduct(UUID id) {
        Product product = getEntityById(id);
        productRepository.delete(product);
        log.info(String.format("Deleted product with id - %s", id));
        return productMapper.mapToDTO(product);
    }

    private Product getEntityById(UUID id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ContentNotFoundException(String.format("Product with id - %s not found!", id)));
    }
}
