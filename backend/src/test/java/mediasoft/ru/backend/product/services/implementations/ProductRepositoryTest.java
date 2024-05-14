package mediasoft.ru.backend.product.services.implementations;

import mediasoft.ru.backend.criteria.condition.Condition;
import mediasoft.ru.backend.criteria.condition.StringCondition;
import mediasoft.ru.backend.enums.CriteriaOptions;
import mediasoft.ru.backend.enums.ProductCategory;
import mediasoft.ru.backend.models.dto.ProductDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.services.product.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan(basePackages = {
        "mediasoft.ru.backend.services.product",
        "mediasoft.ru.backend.services.currency",
        "mediasoft.ru.backend.providers",
        "mediasoft.ru.backend.models.mappers",
        "mediasoft.ru.backend.configurations",
        "mediasoft.ru.backend.criteria"
})
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    private ProductServiceImpl productService;

    private final List<ProductDTO> products = new ArrayList<>() {{
        add(ProductDTO.builder()
                .article("p-1")
                .name("product-1")
                .description("some description for product-1")
                .category(ProductCategory.OTHER)
                .price(BigDecimal.valueOf(101.0))
                .count(BigDecimal.valueOf(1))
                .build());
        add(ProductDTO.builder()
                .article("p-2")
                .name("product-2")
                .description("some description for product-2")
                .category(ProductCategory.OTHER)
                .price(BigDecimal.valueOf(100.0))
                .count(BigDecimal.valueOf(2))
                .build());
        add(ProductDTO.builder()
                .article("p-t-d")
                .name("product-to-delete")
                .description("some description for product-to-delete")
                .category(ProductCategory.OTHER)
                .price(BigDecimal.valueOf(100.0))
                .count(BigDecimal.valueOf(3))
                .build());
    }};

    @Test
    public void select_UsingSpecification_WillReturnOneProduct() {
        products.forEach(product -> productService.createProduct(product));

        Pageable pageable = PageRequest.of(0, 10);
        final List<Condition<?>> conditions = List.of(
                StringCondition.builder()
                        .field("article")
                        .value("p")
                        .operation(CriteriaOptions.LIKE)
                        .build()
        );

        List<ProductInfoResponseDTO> products = productService.searchProducts(pageable, conditions);

        products.forEach(product -> assertThat(product.getName()).contains("p"));
    }
}
