package mediasoft.ru.backend.product.services.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mediasoft.ru.backend.exceptions.ContentNotFoundException;
import mediasoft.ru.backend.exceptions.EmptyFieldException;
import mediasoft.ru.backend.exceptions.UniqueFieldException;
import mediasoft.ru.backend.product.models.dto.CreateProductDTO;
import mediasoft.ru.backend.product.models.dto.ProductDTO;
import mediasoft.ru.backend.product.models.dto.UpdateProductDTO;
import mediasoft.ru.backend.product.models.entities.Product;
import mediasoft.ru.backend.product.models.mappers.ProductMapper;
import mediasoft.ru.backend.product.repositories.ProductRepository;
import mediasoft.ru.backend.product.services.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
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
     * @param createProductDTO модель для создания продукта, которая содержит поля, необходимые для заполнения.
     * @return DTO продукта поле сохранения в базе данных, где проставлен id и дата создания.
     */
    @Override
    public ProductDTO createProduct(CreateProductDTO createProductDTO) {
        if (isNullOrEmpty(createProductDTO.getName()) || isNullOrEmpty(createProductDTO.getArticle()))
            throw new EmptyFieldException();

        if (productRepository.findByArticle(createProductDTO.getArticle()).isPresent())
            throw new UniqueFieldException(createProductDTO.getArticle());

        Product product = productMapper.mapToModel(createProductDTO);
        product.setCreationDate(LocalDate.now());
        Product savedProduct = productRepository.save(product);
        log.info(String.format("Saved new product with id - %s", savedProduct.getId()));
        return productMapper.mapToDTO(savedProduct);
    }

    /**
     * Метод нуден для получения всех записей продуктов из базы данных.
     * После получения всех сущностей они все конвертируется в DTO и после этого возвращаются.
     *
     * @return список всех продуктов, которые хранятся в базе данных, конвертированный в список DTO.
     */
    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream().map(productMapper::mapToDTO).toList();
    }

    /**
     * Метод нужен для поиска сущности базы данных по id, если по переданному id нет сущности, то выбросится ошибка.
     * После того как сущность найдена, она конвертируется в DTO.
     *
     * @param id id продукта, который необходимо получить.
     * @return сущность продукта из базы данных, конвертированная в DTO.
     */
    @Override
    public ProductDTO getProductById(UUID id) {
        Product product = getEntityById(id);
        return productMapper.mapToDTO(product);
    }

    /**
     * Метод нужен для обновления полей продукта. В переданной DTO для обновления содержатся поля, которые необходимо обновить.
     * Сначала идет поиск в базе данных существующей сущности для обновления. После этого идет проверка на то, что новый артикул, если он есть, уникален.
     * После идет проверка на существование полей в переданной сущности: если в переданной DTO нет каких-то полей, то вставляются значения этих полей, которые были до этого.
     * После измененная сущность сохраняется в базе данных и потом конвертируется в DTO.
     *
     * @param updateProductDTO DTO, содержащая необходимые поля для обновления существующей сущности.
     * @return обновленная сущность из базы данных, конвертированная в DTO.
     */
    @Override
    public ProductDTO updateProduct(UpdateProductDTO updateProductDTO) {
        Product sourceProduct = getEntityById(updateProductDTO.getId());

        Optional<Product> sameArticleProduct = productRepository.findByArticle(updateProductDTO.getArticle());
        sameArticleProduct.ifPresent(p -> {
            if (!Objects.equals(p.getArticle(), sourceProduct.getArticle()))
                throw new UniqueFieldException(p.getArticle());
        });

        sourceProduct.setArticle(isNullOrEmpty(updateProductDTO.getArticle())
                ? sourceProduct.getArticle()
                : updateProductDTO.getArticle());

        sourceProduct.setName(isNullOrEmpty(updateProductDTO.getName())
                ? sourceProduct.getName()
                : updateProductDTO.getName());

        sourceProduct.setDescription(updateProductDTO.getDescription() != null
                ? updateProductDTO.getDescription()
                : sourceProduct.getDescription());

        sourceProduct.setCategory(updateProductDTO.getCategory() != null
                ? updateProductDTO.getCategory()
                : sourceProduct.getCategory());

        sourceProduct.setPrice(updateProductDTO.getPrice() != null
                ? updateProductDTO.getPrice()
                : sourceProduct.getPrice());

        if (updateProductDTO.getCount() != null)
            if (!Objects.equals(sourceProduct.getCount(), updateProductDTO.getCount())) {
                sourceProduct.setCount(updateProductDTO.getCount());
                sourceProduct.setLastModifiedDate(LocalDateTime.now());
            }

        Product updatedProduct = productRepository.save(sourceProduct);
        return productMapper.mapToDTO(updatedProduct);
    }

    /**
     * Метод нужен для удаления продукта из базы данных по переданному id.
     *
     * @param id id продукта, который необходимо удалить.
     * @return удаленная из базы данных сущность.
     */
    @Override
    public ProductDTO deleteProduct(UUID id) {
        Product product = getEntityById(id);
        productRepository.delete(product);
        log.info(String.format("Deleted product with id - %s", id));
        return productMapper.mapToDTO(product);
    }

    /**
     * Метод нужен для поиска сущности базы данных по id, если по переданному id нет сущности, то выбросится ошибка.
     *
     * @param id id продукта, который необходимо получить.
     * @return сущность продукта из базы данных.
     */
    private Product getEntityById(UUID id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ContentNotFoundException(String.format("Product with id - %s not found!", id)));
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
