package proyecto.integrador.app.interfaces.aws;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public interface IS3Service {
    String createBucket(String bucketName);
    String checkIfBucketExist(String bucketName);
    List<String> getAllBuckets();
    Boolean uploadFile(String bucketName, String key, Path fileLocation);

    void downloadFile(String bucket, String key) throws IOException;

    String generatePreSignedUploadUrl(String bucketName, String key, Duration duration);
    String generatePreSignedDownloadUrl(String bucketName, String key, Duration duration);
}
