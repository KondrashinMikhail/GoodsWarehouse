package mediasoft.ru.backend.product.services.implementations;

import mediasoft.ru.backend.dto.request.product.CreateProductRequestDTO;
import mediasoft.ru.backend.dto.request.product.UpdateProductRequestDTO;
import mediasoft.ru.backend.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.dto.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.entities.Product;
import mediasoft.ru.backend.enums.ProductCategory;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.exceptions.EmptyFieldException;
import mediasoft.ru.backend.exceptions.UniqueFieldException;
import mediasoft.ru.backend.mappers.ProductMapper;
import mediasoft.ru.backend.repositories.ProductRepository;
import mediasoft.ru.backend.services.product.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Spy
    private ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    private final List<Product> products = new ArrayList<>() {{
        add(Product.builder()
                .id(UUID.fromString("36efa2dc-6620-4969-99d5-1dfafd4cc00a"))
                .article("p-1")
                .name("product-1")
                .description("some description for product-1")
                .category(ProductCategory.OTHER)
                .price(BigDecimal.valueOf(100.0))
                .count(BigDecimal.valueOf(1))
                .creationDate(LocalDate.now().minusYears(1))
                .lastModifiedDate(LocalDateTime.now().minusMonths(1))
                .build());
        add(Product.builder()
                .id(UUID.fromString("e21a9304-b46a-4627-a767-a13ddebc4ff8"))
                .article("p-2")
                .name("product-2")
                .description("some description for product-2")
                .category(ProductCategory.OTHER)
                .price(BigDecimal.valueOf(100.0))
                .count(BigDecimal.valueOf(2))
                .creationDate(LocalDate.now().minusYears(2))
                .lastModifiedDate(LocalDateTime.now().minusMonths(2))
                .build());
        add(Product.builder()
                .id(UUID.fromString("3fa7a6e8-e627-488e-8a12-d641d512196b"))
                .article("p-t-d")
                .name("product-to-delete")
                .description("some description for product-to-delete")
                .category(ProductCategory.OTHER)
                .price(BigDecimal.valueOf(100.0))
                .count(BigDecimal.valueOf(3))
                .creationDate(LocalDate.now().minusYears(3))
                .lastModifiedDate(LocalDateTime.now().minusMonths(3))
                .build());
    }};

    private CreateProductRequestDTO generateCreateProductDTO(String article, String name) {
        return CreateProductRequestDTO.builder()
                .article(article)
                .name(name)
                .description(String.format("some description for %s", name))
                .category(ProductCategory.OTHER)
                .price(BigDecimal.valueOf(100.0))
                .count(BigDecimal.valueOf(1))
                .build();
    }

    private Product getRandomProduct() {
        return products.get(new Random().nextInt(products.size()));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(productRepository.save(ArgumentMatchers.any(Product.class)))
                .thenAnswer(i -> {
                    Product passedProduct = i.getArgument(0);
                    if (passedProduct.getId() == null) passedProduct.setId(UUID.randomUUID());
                    return passedProduct;
                });

        Mockito.when(productRepository.findById(ArgumentMatchers.any(UUID.class)))
                .thenAnswer(i -> {
                    UUID id = i.getArgument(0);
                    return products.stream().filter(p -> p.getId() == id).findFirst();
                });

        Mockito.when(productRepository.findByArticle(ArgumentMatchers.any(String.class)))
                .thenAnswer(i -> {
                    String article = i.getArgument(0);
                    return products.stream().filter(p -> Objects.equals(p.getArticle(), article)).findAny();
                });
    }

    //--------------------| Tests on create product |--------------------

    @Test
    void createProduct_WillDefineIdAndDateCreated() {
        CreateProductRequestDTO createProductRequestDTO = generateCreateProductDTO("p-t", "product-test");
        CreateProductResponseDTO product = productService.createProduct(productMapper.mapToDTO(createProductRequestDTO));

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(product.getCreationDate(), LocalDate.now());
    }

    @Test
    void createProduct_WithNotUniqueArticle_WillFailWithException() {
        CreateProductRequestDTO createProductRequestDTO = generateCreateProductDTO(getRandomProduct().getArticle(), "product-test");

        Assertions.assertThrows(UniqueFieldException.class, () -> productService.createProduct(productMapper.mapToDTO(createProductRequestDTO)));
    }

    @Test
    void createProduct_WithBlankOrNullNameOrArticle_WillFailWithException() {
        CreateProductRequestDTO createProductRequestDTOWithBlankName = generateCreateProductDTO("p-t", "");
        CreateProductRequestDTO createProductRequestDTOWithBlankArticle = generateCreateProductDTO("", "product-test");

        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(productMapper.mapToDTO(createProductRequestDTOWithBlankName)));
        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(productMapper.mapToDTO(createProductRequestDTOWithBlankArticle)));
    }

    @Test
    void createProduct_WithNullNameOrArticle_WillFailWithException() {
        CreateProductRequestDTO createProductRequestDTOWithNullName = generateCreateProductDTO("p-t", null);
        CreateProductRequestDTO createProductRequestDTOWithNullArticle = generateCreateProductDTO(null, "");

        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(productMapper.mapToDTO(createProductRequestDTOWithNullName)));
        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(productMapper.mapToDTO(createProductRequestDTOWithNullArticle)));
    }

    //--------------------| Tests on get product |--------------------

    @Test
    void getProductById_WithExistingId_WillReturnProduct() {
        Product randomProduct = getRandomProduct();
        ProductInfoResponseDTO product = productService.getProductById(randomProduct.getId());

        Assertions.assertNotNull(product);
        Assertions.assertEquals(randomProduct.getArticle(), product.getArticle());
    }

    @Test
    void getProductById_WithNotExistingId_WillFailWithException() {
        Assertions.assertThrows(ContentNotFoundException.class, () -> productService.getProductById(UUID.randomUUID()));
    }

    //--------------------| Tests on update product |--------------------

    @Test
    void updateProduct_WithOneFieldFilled_WillUpdateOneField() {
        String testDescription = String.format(("This description is specified to update product from %s"), LocalDateTime.now());
        Product sourceProduct = getRandomProduct();
        UpdateProductRequestDTO updateProductRequestDTO = UpdateProductRequestDTO.builder()
                .id(sourceProduct.getId())
                .description(testDescription)
                .build();
        UpdateProductResponseDTO resultProduct = productService.updateProduct(productMapper.mapToDTO(updateProductRequestDTO));

        Assertions.assertNotNull(resultProduct.getId());
        Assertions.assertEquals(testDescription, resultProduct.getDescription());
    }

    @Test
    void updateProduct_WithEmptyNameOrArticle_WillNotUpdateNameOrArticle() {
        String testDescription = String.format(("This description is specified to update product from %s"), LocalDateTime.now());
        Product sourceProduct = getRandomProduct();
        UpdateProductRequestDTO updateProductRequestDTO = UpdateProductRequestDTO.builder()
                .id(sourceProduct.getId())
                .description(testDescription)
                .article("")
                .name("")
                .build();
        UpdateProductResponseDTO resultProduct = productService.updateProduct(productMapper.mapToDTO(updateProductRequestDTO));

        Assertions.assertEquals(sourceProduct.getArticle(), resultProduct.getArticle());
        Assertions.assertEquals(sourceProduct.getName(), resultProduct.getName());
    }

    @Test
    void updateProduct_WithNewCount_WillChangeLastModifiedDate() {
        Product sourceProduct = getRandomProduct();
        UpdateProductRequestDTO updateProductRequestDTO = UpdateProductRequestDTO.builder()
                .id(sourceProduct.getId())
                .count(sourceProduct.getCount().add(BigDecimal.valueOf(1)))
                .build();
        UpdateProductResponseDTO resultProduct = productService.updateProduct(productMapper.mapToDTO(updateProductRequestDTO));

        Assertions.assertEquals(
                resultProduct.getLastModifiedDate().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    void updateProduct_WithNotUniqueNewArticle_WillFailWithException() {
        Product sourceProduct = getRandomProduct();
        String testArticle = products.stream()
                .filter(p -> p.getId() != sourceProduct.getId())
                .findFirst()
                .map(Product::getArticle)
                .orElse(null);
        UpdateProductRequestDTO updateProductRequestDTO = UpdateProductRequestDTO.builder()
                .id(sourceProduct.getId())
                .article(testArticle)
                .build();

        Assertions.assertThrows(UniqueFieldException.class, () -> productService.updateProduct(productMapper.mapToDTO(updateProductRequestDTO)));
    }

    @Test
    void updateProduct_WithNotFilledId_WillFailWithException() {
        UpdateProductRequestDTO updateProductRequestDTO = UpdateProductRequestDTO.builder()
                .description(String.format(("This description is specified to update product from %s"), LocalDateTime.now()))
                .build();

        Assertions.assertThrows(ContentNotFoundException.class, () -> productService.updateProduct(productMapper.mapToDTO(updateProductRequestDTO)));
    }

    @Test
    void updateProduct_WithNotExistingId_WillFailWithException() {
        UpdateProductRequestDTO updateProductRequestDTO = UpdateProductRequestDTO.builder()
                .id(UUID.randomUUID())
                .description(String.format(("This description is specified to update product from %s"), LocalDateTime.now()))
                .build();

        Assertions.assertThrows(ContentNotFoundException.class, () -> productService.updateProduct(productMapper.mapToDTO(updateProductRequestDTO)));
    }

    //--------------------| Tests on delete product |--------------------

    @Test
    void deleteProduct_WithNotExistingId_WillFailWithException() {
        Assertions.assertThrows(ContentNotFoundException.class, () -> productService.deleteProduct(UUID.randomUUID()));
    }
}