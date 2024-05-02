package mediasoft.ru.backend.product.services.implementations;

import mediasoft.ru.backend.criteria.Condition;
import mediasoft.ru.backend.product.models.dto.CreateProductDTO;
import mediasoft.ru.backend.product.models.dto.ProductDTO;
import mediasoft.ru.backend.product.models.entities.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ComponentScan(basePackages = {"mediasoft.ru.backend.product.services.implementations"})
@ComponentScan(basePackages = {"mediasoft.ru.backend.product.models.mappers"})
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    private ProductServiceImpl productService;

    private final List<CreateProductDTO> products = new ArrayList<>() {{
        add(CreateProductDTO.builder()
                .article("p-1")
                .name("product-1")
                .description("some description for product-1")
                .category(ProductCategory.OTHER)
                .price(101.0)
                .count(1)
                .build());
        add(CreateProductDTO.builder()
                .article("p-2")
                .name("product-2")
                .description("some description for product-2")
                .category(ProductCategory.OTHER)
                .price(100.0)
                .count(2)
                .build());
        add(CreateProductDTO.builder()
                .article("p-t-d")
                .name("product-to-delete")
                .description("some description for product-to-delete")
                .category(ProductCategory.OTHER)
                .price(100.0)
                .count(3)
                .build());
    }};

    @Test
    public void select_UsingSpecification_WillReturnOneProduct() {
        products.forEach(product -> productService.createProduct(product));

        Pageable pageable = PageRequest.of(0, 10);
        List<Condition> conditions = List.of(
                new Condition("article", "p", "~"),
                new Condition("price", 100.0, ">")
        );

        List<ProductDTO> products = productService.searchProducts(pageable, conditions);

        products.forEach(product -> {
            assertThat(product.getName()).contains("p");
            assertThat(product.getPrice()).isGreaterThan(100.0);
        });
    }
}
