package mediasoft.ru.backend.services.filestorage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import mediasoft.ru.backend.configurations.MinioProperties;
import mediasoft.ru.backend.models.entities.Image;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class MinioFileStorageImpl implements FileStorageService {
    private MinioClient minioClient;
    private MinioProperties minioProperties;

    @PostConstruct
    @SneakyThrows
    public void initDefaultBucket() {
        createBucket(minioProperties.getBucket());
    }

    @SneakyThrows
    public void createBucket(String bucketName) {
        boolean isExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExists) minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    @Override
    @SneakyThrows
    public void uploadFile(String path, byte[] content) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(path)
                .stream(new ByteArrayInputStream(content), content.length, -1)
                .build());
    }

    @Override
    @SneakyThrows
    public byte[] getZip(String archiveName, List<Image> images) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archiveName))) {
            for (String path : images.stream().map(Image::getPath).toList()) {
                String[] splitPath = path.split("/");
                byte[] bytes = getFile(path);
                ZipEntry zipEntry = new ZipEntry(splitPath[splitPath.length - 1]);
                zos.putNextEntry(zipEntry);
                zos.write(bytes);
            }
        }

        return Files.readAllBytes(Paths.get(archiveName));
    }

    @Override
    @SneakyThrows
    public byte[] getFile(String key) {
        return IOUtils.toByteArray(minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(key)
                .build()));
    }
}
