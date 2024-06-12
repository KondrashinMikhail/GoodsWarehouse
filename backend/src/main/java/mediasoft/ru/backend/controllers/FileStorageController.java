package mediasoft.ru.backend.controllers;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import mediasoft.ru.backend.models.entities.Image;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@AllArgsConstructor
public class FileStorageController {
    private FileStorageService fileStorageService;
    private ImageService imageService;

    @SneakyThrows
    @PostMapping("/attach/{productId}")
    public ResponseEntity uploadFile(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file
    ) {
        String fileName = file.getOriginalFilename();
        String path = String.format("%s/%s", productId, fileName);
        fileStorageService.uploadFile(path, file.getBytes());
        imageService.attachImage(productId, path);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "/product/{productId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable UUID productId) {
        String archiveName = String.format("%s.zip", imageService.getProductName(productId));
        List<Image> images = imageService.getImages(productId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", archiveName))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileStorageService.getZip(archiveName, images));
    }
}
