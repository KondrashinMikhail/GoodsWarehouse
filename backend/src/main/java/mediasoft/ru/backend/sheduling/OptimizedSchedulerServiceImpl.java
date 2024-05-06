package mediasoft.ru.backend.sheduling;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mediasoft.ru.backend.annotations.TimeMeasuring;
import mediasoft.ru.backend.entities.Product;
import mediasoft.ru.backend.repositories.ProductRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${app.scheduling.enabled} and ${app.scheduling.optimization}")
public class OptimizedSchedulerServiceImpl implements SchedulerService {
    private final ProductRepository productRepository;

    @Autowired
    private final EntityManager entityManager;

    @Value("${app.scheduling.price_increase}")
    private BigDecimal INCREASE_PERCENT;

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

            products.forEach(this::increasePrice);

            productRepository.saveAll(products);
            entityManager.flush();
            entityManager.clear();
        }

        session.setJdbcBatchSize(null);
    }

    private void increasePrice(Product product) {
        BigDecimal sourcePrice = product.getPrice();
        BigDecimal newPrice = sourcePrice.add((sourcePrice.multiply(INCREASE_PERCENT)));
        product.setPrice(newPrice);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt", true))) {
            writer.write(product.toString() + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
