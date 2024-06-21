package mediasoft.ru.backend.services.filestorage;

import mediasoft.ru.backend.models.dto.GetMinioFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.UUID;

public interface FileStorageService {
    UUID uploadFile(MultipartFile file);

    void getZip(UUID productId, OutputStream outputStream);

    GetMinioFileDTO getFile(String key);
}
