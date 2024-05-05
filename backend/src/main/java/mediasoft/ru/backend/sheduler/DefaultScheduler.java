package mediasoft.ru.backend.sheduler;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mediasoft.ru.backend.product.models.entities.Product;
import mediasoft.ru.backend.product.repositories.ProductRepository;
import mediasoft.ru.backend.annotations.TimeMeasuring;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${app.scheduling.enabled} and not ${app.scheduling.optimization}")
public class DefaultScheduler implements SchedulerService {
    private final ProductRepository productRepository;

    @Value("${app.scheduling.price_increase}")
    private Double INCREASE_PERCENT;

    @Override
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    @Transactional
    @TimeMeasuring
    public void raisePrice() {
        List<Product> products = productRepository.findAll();
        products.forEach(this::increasePrice);
        productRepository.saveAll(products);
    }

    private void increasePrice(Product product) {
        double sourcePrice = product.getPrice();
        double newPrice = sourcePrice + (sourcePrice * INCREASE_PERCENT);
        product.setPrice(newPrice);
    }
}
