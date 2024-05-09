package mediasoft.ru.backend.services.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mediasoft.ru.backend.exceptions.AddProductToOrderException;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.exceptions.EmptyFieldException;
import mediasoft.ru.backend.exceptions.UniqueFieldException;
import mediasoft.ru.backend.models.dto.ProductDTO;
import mediasoft.ru.backend.models.dto.request.product.ProductInOrderRequestDTO;
import mediasoft.ru.backend.models.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInOrderResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.models.entities.Product;
import mediasoft.ru.backend.models.mappers.ProductMapper;
import mediasoft.ru.backend.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Метод для создания продукта. Принимает DTO c полям, которые необходимы к заполнению при создании.
     * Сначала проверяется на null или пустоту/не заполненность имени продукта и его артикула. После идет проверка на уникальность артикула в базе данных.
     * После идет превращение в модель базы данных из DTO, подстановка даты создания как текущей даты. Поле последнего изменения количества не инициализируется.
     * Далее сохраняется в базу данных и сохраненный продукт с проставленным id конвертируется в DTO и возвращается.
     *
     * @param createProductRequestDTO модель для создания продукта, которая содержит поля, необходимые для заполнения.
     * @return DTO продукта поле сохранения в базе данных, где проставлен id и дата создания.
     */
    @Override
    public CreateProductResponseDTO createProduct(ProductDTO createProductRequestDTO) {
        if (isNullOrEmpty(createProductRequestDTO.getName()) || isNullOrEmpty(createProductRequestDTO.getArticle()))
            throw new EmptyFieldException();

        if (productRepository.findByArticle(createProductRequestDTO.getArticle()).isPresent())
            throw new UniqueFieldException(createProductRequestDTO.getArticle());

        Product product = productMapper.mapToModel(createProductRequestDTO);
        product.setCreationDate(LocalDate.now());
        Product savedProduct = productRepository.save(product);
        log.info(String.format("Saved new product with id - %s", savedProduct.getId()));
        return productMapper.mapModelToResponse(savedProduct);
    }

    /**
     * Метод нуден для получения всех записей продуктов из базы данных.
     * После получения всех сущностей они все конвертируется в DTO и после этого возвращаются.
     *
     * @return список всех продуктов, которые хранятся в базе данных, конвертированный в список DTO.
     */
    @Override
    public List<ProductInfoResponseDTO> getAllProducts(Pageable pageable) {
        Page<Product> allProducts = productRepository.findAll(pageable);
        return allProducts.stream().map(productMapper::mapModelToInfoResponse).toList();
    }

    /**
     * Метод нужен для поиска сущности базы данных по id, если по переданному id нет сущности, то выбросится ошибка.
     * После того как сущность найдена, она конвертируется в DTO.
     *
     * @param id id продукта, который необходимо получить.
     * @return сущность продукта из базы данных, конвертированная в DTO.
     */
    @Override
    public ProductInfoResponseDTO getProductById(UUID id) {
        Product product = getEntityById(id);
        return productMapper.mapModelToInfoResponse(product);
    }

    /**
     * Метод нужен для обновления полей продукта. В переданной DTO для обновления содержатся поля, которые необходимо обновить.
     * Сначала идет поиск в базе данных существующей сущности для обновления. После этого идет проверка на то, что новый артикул, если он есть, уникален.
     * После идет проверка на существование полей в переданной сущности: если в переданной DTO нет каких-то полей, то вставляются значения этих полей, которые были до этого.
     * После измененная сущность сохраняется в базе данных и потом конвертируется в DTO.
     *
     * @param updateProductRequestDTO DTO, содержащая необходимые поля для обновления существующей сущности.
     * @return обновленная сущность из базы данных, конвертированная в DTO.
     */
    @Override
    public UpdateProductResponseDTO updateProduct(ProductDTO updateProductRequestDTO) {
        Product sourceProduct = getEntityById(updateProductRequestDTO.getId());

        Optional<Product> sameArticleProduct = productRepository.findByArticle(updateProductRequestDTO.getArticle());
        sameArticleProduct.ifPresent(p -> {
            if (!Objects.equals(p.getArticle(), sourceProduct.getArticle()))
                throw new UniqueFieldException(p.getArticle());
        });

        sourceProduct.setArticle(isNullOrEmpty(updateProductRequestDTO.getArticle())
                ? sourceProduct.getArticle()
                : updateProductRequestDTO.getArticle());

        sourceProduct.setName(isNullOrEmpty(updateProductRequestDTO.getName())
                ? sourceProduct.getName()
                : updateProductRequestDTO.getName());

        sourceProduct.setDescription(updateProductRequestDTO.getDescription() != null
                ? updateProductRequestDTO.getDescription()
                : sourceProduct.getDescription());

        sourceProduct.setCategory(updateProductRequestDTO.getCategory() != null
                ? updateProductRequestDTO.getCategory()
                : sourceProduct.getCategory());

        sourceProduct.setPrice(updateProductRequestDTO.getPrice() != null
                ? updateProductRequestDTO.getPrice()
                : sourceProduct.getPrice());

        if (updateProductRequestDTO.getCount() != null)
            if (!Objects.equals(sourceProduct.getCount(), updateProductRequestDTO.getCount())) {
                sourceProduct.setCount(updateProductRequestDTO.getCount());
                sourceProduct.setLastModifiedDate(LocalDateTime.now());
            }

        Product updatedProduct = productRepository.save(sourceProduct);
        return productMapper.mapModelToUpdateResponse(updatedProduct);
    }

    /**
     * Метод нужен для удаления продукта из базы данных по переданному id.
     *
     * @param id id продукта, который необходимо удалить.
     */
    @Override
    public void deleteProduct(UUID id) {
        Product product = getEntityById(id);
        product.setIsAvailable(false);
        productRepository.save(product);
        log.info(String.format("Made product with id - %s not available", id));
    }

    /**
     * Метод нужен для поиска сущности базы данных по id, если по переданному id нет сущности, то выбросится ошибка.
     *
     * @param id id продукта, который необходимо получить.
     * @return сущность продукта из базы данных.
     */
    @Override
    public Product getEntityById(UUID id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ContentNotFoundException(String.format("Product with id - %s not found!", id)));
    }

    @Override
    public void checkProductBeforeAddToOrder(List<ProductInOrderRequestDTO> productInOrderRequestDTO) {
        for (ProductInOrderRequestDTO productInOrder : productInOrderRequestDTO) {
            Product product = getEntityById(productInOrder.getId());
            if (!product.getIsAvailable())
                throw new AddProductToOrderException(
                        String.format("Can not add product %s to order because this product is not available!", product.getId()));
            if (product.getCount().compareTo(productInOrder.getCount()) < 0)
                throw new AddProductToOrderException(
                        String.format("Can not add product %s to order because there are not enough of them!", product.getId()));
        }
    }

    @Override
    public void decrementProductCount(UUID productId, BigDecimal count) {
        Product product = getEntityById(productId);
        product.setCount(product.getCount().subtract(count));
        productRepository.save(product);
        log.info("Subtracted {} of product with id - {}", count, productId);
    }

    @Override
    public List<ProductInOrderResponseDTO> getProductsInOrder(UUID orderId) {
        return productRepository.getOrderProducts(orderId).stream()
                .map(productMapper::mapProjectionToResponse)
                .toList();
    }

    @Override
    public void returnProduct(UUID productId, BigDecimal count) {
        Product product = getEntityById(productId);
        product.setCount(product.getCount().add(count));
        productRepository.save(product);
        log.info("Returned {} products with id - {}", count, productId);
    }

    /**
     * Метод валидирует входную строку на пустоту и незаполненность.
     *
     * @param str исходная строка, которую необходимо проверить.
     * @return true, если строка является null, или пустая/не заполненная, false - в обратном случае.
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }
}
