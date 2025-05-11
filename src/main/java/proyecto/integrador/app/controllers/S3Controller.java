package proyecto.integrador.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proyecto.integrador.app.interfaces.aws.IS3Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("s3")
public class S3Controller {

    @Value("${spring.destination.folder}")
    private String destinationFolder;

    @Autowired
    private IS3Service s3Service;

    @PostMapping("/create")
    public ResponseEntity<String> createBucket(@RequestParam String bucketName) {
        return ResponseEntity.ok(this.s3Service.createBucket(bucketName));
    }

    @GetMapping("/check/{bucketName}")
    public ResponseEntity<String> checkBucket(@PathVariable String bucketName) {
        return ResponseEntity.ok(this.s3Service.checkIfBucketExist(bucketName));
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listBuckets() {
        return ResponseEntity.ok(this.s3Service.getAllBuckets());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam String bucketName, @RequestParam String key, @RequestPart MultipartFile file) throws IOException {
        try {
            Path staticDir = Paths.get(destinationFolder);
            if (!Files.exists(staticDir)) {
                Files.createDirectories(staticDir);
            }

            Path filePath = staticDir.resolve(file.getOriginalFilename());
            Path finalPath = Files.write(filePath, file.getBytes());

            Boolean result = this.s3Service.uploadFile(bucketName, key, finalPath);

            if (result) {
                Files.delete(filePath);
                return ResponseEntity.ok("Archivo cargado correctamente");
            } else {
                return ResponseEntity.internalServerError().body("Error al cargar el archivo al bucket");
            }
        } catch (IOException e) {
            throw new IOException("Error al procesar el archivo.");
        }
    }

    @PostMapping("/download")
    public ResponseEntity<String> downloadFile(@RequestParam String bucketName, @RequestParam String key) throws IOException {
        this.s3Service.downloadFile(bucketName, key);
        return ResponseEntity.ok("Archivo descargado correctamente");
    }

    @PostMapping("/upload/pre-signed")
    public ResponseEntity<String> generatePreSignedUploadUrl(@RequestParam String bucketName, @RequestParam String key, @RequestParam Long time) {
        Duration durationToLive = Duration.ofMinutes(time);
        return ResponseEntity.ok(this.s3Service.generatePreSignedUploadUrl(bucketName, key, durationToLive));
    }

    @PostMapping("/download/pre-signed")
    public ResponseEntity<String> generatePreSignedDownloadUrl(@RequestParam String bucketName, @RequestParam String key, @RequestParam Long time) {
        Duration durationToLive = Duration.ofMinutes(time);
        return ResponseEntity.ok(this.s3Service.generatePreSignedDownloadUrl(bucketName, key, durationToLive));
    }
}
