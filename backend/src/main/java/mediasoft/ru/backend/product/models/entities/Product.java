package mediasoft.ru.backend.product.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String article;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer count;

    @Column(updatable = false)
    private LocalDate creationDate;

    private LocalDateTime lastModifiedDate;
}
