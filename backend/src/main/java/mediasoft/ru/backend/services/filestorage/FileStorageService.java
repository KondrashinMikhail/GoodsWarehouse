package mediasoft.ru.backend.services.filestorage;

import mediasoft.ru.backend.models.entities.Image;

import java.util.List;

public interface FileStorageService {
    void uploadFile(String path, byte[] content);

    byte[] getZip(String archiveName, List<Image> images);

    byte[] getFile(String key);
}
