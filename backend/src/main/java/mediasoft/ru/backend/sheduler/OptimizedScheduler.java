package mediasoft.ru.backend.sheduler;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mediasoft.ru.backend.product.models.entities.Product;
import mediasoft.ru.backend.product.repositories.ProductRepository;
import mediasoft.ru.backend.annotations.TimeMeasuring;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${app.scheduling.enabled} and ${app.scheduling.optimization}")
public class OptimizedScheduler implements SchedulerService {
    private final ProductRepository productRepository;

    @Autowired
    private final EntityManager entityManager;

    @Value("${app.scheduling.price_increase}")
    private Double INCREASE_PERCENT;

    @Override
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    @Transactional
    @TimeMeasuring
    public void raisePrice() {
        long countOfAllProducts = productRepository.count();
        int optimalProductsPart = (int) (countOfAllProducts / 5);

        Session session = entityManager.unwrap(Session.class);
        int optimalBatchSize = 50 * (optimalProductsPart / 10000);
        session.setJdbcBatchSize(optimalBatchSize);

        for (int i = 0; i < countOfAllProducts; i += optimalProductsPart) {
            int offset = optimalProductsPart * (i / optimalProductsPart);

            ArrayList<Product> products = productRepository.findAllWithLimit(optimalProductsPart, offset);

            products.forEach(product -> {
                double sourcePrice = product.getPrice();
                double newPrice = sourcePrice + (sourcePrice * INCREASE_PERCENT);
                product.setPrice(newPrice);
            });

            productRepository.saveAll(products);
            entityManager.flush();
            entityManager.clear();
        }

        session.setJdbcBatchSize(null);
    }
}
