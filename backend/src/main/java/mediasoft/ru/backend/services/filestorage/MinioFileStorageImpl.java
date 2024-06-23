package mediasoft.ru.backend.services.filestorage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mediasoft.ru.backend.configurations.MinioProperties;
import mediasoft.ru.backend.exceptions.FileException;
import mediasoft.ru.backend.exceptions.MinioException;
import mediasoft.ru.backend.models.GetMinioFileDTO;
import mediasoft.ru.backend.persistence.entities.Image;
import mediasoft.ru.backend.services.image.ImageService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class MinioFileStorageImpl implements FileStorageService {
    private MinioClient minioClient;
    private MinioProperties minioProperties;
    private ImageService imageService;

    private final String FILENAME_METADATA_KEY = "filename";

    @PostConstruct
    public void initDefaultBucket() {
        createBucket(minioProperties.getBucket());
    }

    public void createBucket(String bucketName) {
        try {
            boolean isExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExists) minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new MinioException("Error creating bucket!");
        }
    }

    @Override
    public UUID uploadFile(MultipartFile file) {
        try {
            byte[] content = file.getBytes();
            UUID key = UUID.randomUUID();

            Map<String, String> metadata = new HashMap<>() {{
                put(FILENAME_METADATA_KEY, file.getOriginalFilename());
            }};

            minioClient.putObject(PutObjectArgs.builder()
                    .userMetadata(metadata)
                    .bucket(minioProperties.getBucket())
                    .object(key.toString())
                    .stream(new ByteArrayInputStream(content), content.length, -1)
                    .build());

            return key;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new FileException("Error occurred while uploading file!");
        }
    }

    @Override
    public void getZip(UUID productId, OutputStream outputStream) {
        try {
            List<Image> images = imageService.getImages(productId);

            try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                for (String key : images.stream().map(i -> i.getId().toString()).toList()) {
                    GetMinioFileDTO file = getFile(key);
                    ZipEntry zipEntry = new ZipEntry(file.getFilename());
                    zos.putNextEntry(zipEntry);
                    zos.write(file.getContent());
                }
            }
        } catch (IOException e) {
            throw new FileException("Error occurred while getting .zip archive!");
        }
    }

    @Override
    public GetMinioFileDTO getFile(String key) {
        try {
            GetObjectResponse getObjectResponse = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(key)
                    .build());

            StatObjectResponse statObject = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(key)
                    .build());

            return GetMinioFileDTO.builder()
                    .filename(statObject.userMetadata().get(FILENAME_METADATA_KEY))
                    .content(IOUtils.toByteArray(getObjectResponse))
                    .build();

        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new MinioException("Error occurred getting file from Minio!");
        }
    }
}
