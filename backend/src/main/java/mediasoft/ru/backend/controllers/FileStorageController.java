package mediasoft.ru.backend.controllers;

import lombok.AllArgsConstructor;
import mediasoft.ru.backend.services.filestorage.FileStorageService;
import mediasoft.ru.backend.services.image.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileStorageController {
    private FileStorageService fileStorageService;
    private ImageService imageService;

    @PostMapping("/attach/{productId}")
    public ResponseEntity<UUID> uploadFile(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file) {
        UUID generatedKey = fileStorageService.uploadFile(file);
        imageService.attachImage(productId, generatedKey);
        return ResponseEntity.ok(generatedKey);
    }

    @GetMapping(value = "/product/{productId}/zip/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> getFile(@PathVariable UUID productId) {
        StreamingResponseBody responseBody = outputStream -> fileStorageService.getZip(productId, outputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s.zip\"", imageService.getProductName(productId)))
                .body(responseBody);
    }
}
