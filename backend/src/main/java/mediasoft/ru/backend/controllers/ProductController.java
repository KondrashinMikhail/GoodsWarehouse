package mediasoft.ru.backend.controllers;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.criteria.condition.Condition;
import mediasoft.ru.backend.models.dto.request.product.CreateProductRequestDTO;
import mediasoft.ru.backend.models.dto.request.product.UpdateProductRequestDTO;
import mediasoft.ru.backend.models.dto.response.product.CreateProductResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.ProductInfoResponseDTO;
import mediasoft.ru.backend.models.dto.response.product.UpdateProductResponseDTO;
import mediasoft.ru.backend.models.mappers.ProductMapper;
import mediasoft.ru.backend.services.product.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping("/create")
    public ResponseEntity<CreateProductResponseDTO> createProduct(@RequestBody CreateProductRequestDTO createProductRequestDTO) {
        return ResponseEntity.ok(productService.createProduct(productMapper.mapToDTO(createProductRequestDTO)));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ProductInfoResponseDTO>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductInfoResponseDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<UpdateProductResponseDTO> updateProduct(@RequestBody UpdateProductRequestDTO updateProductRequestDTO) {
        return ResponseEntity.ok(productService.updateProduct(productMapper.mapToDTO(updateProductRequestDTO)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<ProductInfoResponseDTO>> searchProducts(
            Pageable pageable,
            @RequestBody List<Condition<?>> conditions) {
        return ResponseEntity.ok(productService.searchProducts(pageable, conditions));
    }
}
