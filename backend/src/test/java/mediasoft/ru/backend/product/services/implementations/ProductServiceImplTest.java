package mediasoft.ru.backend.product.services.implementations;

import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.exceptions.EmptyFieldException;
import mediasoft.ru.backend.exceptions.UniqueFieldException;
import mediasoft.ru.backend.product.models.dto.CreateProductDTO;
import mediasoft.ru.backend.product.models.dto.ProductDTO;
import mediasoft.ru.backend.product.models.dto.UpdateProductDTO;
import mediasoft.ru.backend.product.models.entities.Product;
import mediasoft.ru.backend.product.models.entities.ProductCategory;
import mediasoft.ru.backend.product.models.mappers.ProductMapper;
import mediasoft.ru.backend.product.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
                .price(100.0)
                .count(1)
                .creationDate(LocalDate.now().minusYears(1))
                .lastModifiedDate(LocalDateTime.now().minusMonths(1))
                .build());
        add(Product.builder()
                .id(UUID.fromString("e21a9304-b46a-4627-a767-a13ddebc4ff8"))
                .article("p-2")
                .name("product-2")
                .description("some description for product-2")
                .category(ProductCategory.OTHER)
                .price(100.0)
                .count(2)
                .creationDate(LocalDate.now().minusYears(2))
                .lastModifiedDate(LocalDateTime.now().minusMonths(2))
                .build());
        add(Product.builder()
                .id(UUID.fromString("3fa7a6e8-e627-488e-8a12-d641d512196b"))
                .article("p-t-d")
                .name("product-to-delete")
                .description("some description for product-to-delete")
                .category(ProductCategory.OTHER)
                .price(100.0)
                .count(3)
                .creationDate(LocalDate.now().minusYears(3))
                .lastModifiedDate(LocalDateTime.now().minusMonths(3))
                .build());
    }};

    private CreateProductDTO generateCreateProductDTO(String article, String name) {
        return CreateProductDTO.builder()
                .article(article)
                .name(name)
                .description(String.format("some description for %s", name))
                .category(ProductCategory.OTHER)
                .price(100.0)
                .count(1)
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
        CreateProductDTO createProductDTO = generateCreateProductDTO("p-t", "product-test");
        ProductDTO product = productService.createProduct(createProductDTO);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(product.getCreationDate(), LocalDate.now());
    }

    @Test
    void createProduct_WithNotUniqueArticle_WillFailWithException() {
        CreateProductDTO createProductDTO = generateCreateProductDTO(getRandomProduct().getArticle(), "product-test");

        Assertions.assertThrows(UniqueFieldException.class, () -> productService.createProduct(createProductDTO));
    }

    @Test
    void createProduct_WithBlankOrNullNameOrArticle_WillFailWithException() {
        CreateProductDTO createProductDTOWithBlankName = generateCreateProductDTO("p-t", "");
        CreateProductDTO createProductDTOWithBlankArticle = generateCreateProductDTO("", "product-test");

        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(createProductDTOWithBlankName));
        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(createProductDTOWithBlankArticle));
    }

    @Test
    void createProduct_WithNullNameOrArticle_WillFailWithException() {
        CreateProductDTO createProductDTOWithNullName = generateCreateProductDTO("p-t", null);
        CreateProductDTO createProductDTOWithNullArticle = generateCreateProductDTO(null, "");

        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(createProductDTOWithNullName));
        Assertions.assertThrows(EmptyFieldException.class, () -> productService.createProduct(createProductDTOWithNullArticle));
    }

    //--------------------| Tests on get product |--------------------

    @Test
    void getProductById_WithExistingId_WillReturnProduct() {
        Product randomProduct = getRandomProduct();
        ProductDTO product = productService.getProductById(randomProduct.getId());

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
        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
                .id(sourceProduct.getId())
                .description(testDescription)
                .build();
        ProductDTO resultProduct = productService.updateProduct(updateProductDTO);

        Assertions.assertNotNull(resultProduct.getId());
        Assertions.assertEquals(testDescription, resultProduct.getDescription());
    }

    @Test
    void updateProduct_WithEmptyNameOrArticle_WillNotUpdateNameOrArticle() {
        String testDescription = String.format(("This description is specified to update product from %s"), LocalDateTime.now());
        Product sourceProduct = getRandomProduct();
        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
                .id(sourceProduct.getId())
                .description(testDescription)
                .article("")
                .name("")
                .build();
        ProductDTO resultProduct = productService.updateProduct(updateProductDTO);

        Assertions.assertEquals(sourceProduct.getArticle(), resultProduct.getArticle());
        Assertions.assertEquals(sourceProduct.getName(), resultProduct.getName());
    }

    @Test
    void updateProduct_WithNewCount_WillChangeLastModifiedDate() {
        Product sourceProduct = getRandomProduct();
        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
                .id(sourceProduct.getId())
                .count(sourceProduct.getCount() + 1)
                .build();
        ProductDTO resultProduct = productService.updateProduct(updateProductDTO);

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
        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
                .id(sourceProduct.getId())
                .article(testArticle)
                .build();

        Assertions.assertThrows(UniqueFieldException.class, () -> productService.updateProduct(updateProductDTO));
    }

    @Test
    void updateProduct_WithNotFilledId_WillFailWithException() {
        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
                .description(String.format(("This description is specified to update product from %s"), LocalDateTime.now()))
                .build();

        Assertions.assertThrows(ContentNotFoundException.class, () -> productService.updateProduct(updateProductDTO));
    }

    @Test
    void updateProduct_WithNotExistingId_WillFailWithException() {
        UpdateProductDTO updateProductDTO = UpdateProductDTO.builder()
                .id(UUID.randomUUID())
                .description(String.format(("This description is specified to update product from %s"), LocalDateTime.now()))
                .build();

        Assertions.assertThrows(ContentNotFoundException.class, () -> productService.updateProduct(updateProductDTO));
    }

    //--------------------| Tests on delete product |--------------------

    @Test
    void deleteProduct_WithNotExistingId_WillFailWithException() {
        Assertions.assertThrows(ContentNotFoundException.class, () -> productService.deleteProduct(UUID.randomUUID()));
    }
}