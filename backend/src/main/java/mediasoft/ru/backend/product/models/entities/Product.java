package mediasoft.ru.backend.product.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @NotNull
    @Column(unique = true)
    private String article;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductCategory category;
    @NotNull
    private Double price;
    @NotNull
    private Integer count;
    @NotNull
    @Column(updatable = false)
    private LocalDate creationDate;
    @NotNull
    private LocalDateTime lastModifiedDate;

    @Transient
    private Integer previousCount;

    @PostLoad
    public void initializePreviousCount() {
        this.previousCount = this.count;
    }

    @PrePersist
    public void initializeDate() {
        this.creationDate = LocalDate.now();
    }

    @PreUpdate
    public void changeLastModified() {
        if (!this.count.equals(this.previousCount))
            this.lastModifiedDate = LocalDateTime.now();
    }
}
