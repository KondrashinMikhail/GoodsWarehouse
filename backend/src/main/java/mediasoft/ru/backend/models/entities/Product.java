package mediasoft.ru.backend.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mediasoft.ru.backend.enums.ProductCategory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(unique = true, nullable = false)
    private String article;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal count;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDate creationDate;

    private LocalDateTime lastModifiedDate;

    @Column(nullable = false)
    private Boolean isAvailable;

//    @OneToMany
//    @JoinColumn(name = "product_id")
//    private List<Image> images;

    @PrePersist
    private void defaultFields() {
        if (this.creationDate == null) this.creationDate = LocalDate.now();
        if (this.isAvailable == null) this.isAvailable = true;
    }
}
