package mediasoft.ru.backend.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderProductKey.class)
public class OrderProduct {
    @Id
    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @Column(nullable = false)
    private BigDecimal productPrice;

    @Column(nullable = false)
    private BigDecimal productCount;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}
