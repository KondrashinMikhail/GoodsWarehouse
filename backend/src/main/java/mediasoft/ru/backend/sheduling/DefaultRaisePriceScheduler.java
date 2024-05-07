package mediasoft.ru.backend.sheduling;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mediasoft.ru.backend.models.entities.Product;
import mediasoft.ru.backend.repositories.ProductRepository;
import mediasoft.ru.backend.annotations.TimeMeasuring;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${app.scheduling.enabled} and not ${app.scheduling.optimization}")
public class DefaultRaisePriceScheduler {
    private final ProductRepository productRepository;

    @Value("${app.scheduling.price_increase}")
    private BigDecimal INCREASE_PERCENT;

    @Scheduled(fixedRateString = "${app.scheduling.period}")
    @Transactional
    @TimeMeasuring
    public void raisePrice() {
        List<Product> products = productRepository.findAll();
        products.forEach(this::increasePrice);
        productRepository.saveAll(products);
    }

    private void increasePrice(Product product) {
        BigDecimal sourcePrice = product.getPrice();
        BigDecimal newPrice = sourcePrice.add(sourcePrice.multiply(INCREASE_PERCENT));
        product.setPrice(newPrice);
    }
}
