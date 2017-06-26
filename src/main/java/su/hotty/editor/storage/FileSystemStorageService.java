package su.hotty.editor.storage;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(FileSystemStorageService.class);

    @Value("${resources.files.folder}")
    private String rootFolder;

    private Path rootLocation;

    public FileSystemStorageService() {

    }

    @PostConstruct
    private void postConstruct() {
        init();
    }

    @Override
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file: " + file.getOriginalFilename());
            }

            String newFilename = createFilename(file.getOriginalFilename());
            log.debug("store original filename: {} new filename: {}", file.getOriginalFilename(), newFilename);

            Files.copy(file.getInputStream(), this.rootLocation.resolve(newFilename));

            return newFilename;

        } catch (IOException e) {
            String m = String.format("Failed to store file: %s", file.getOriginalFilename());
            log.error(m, e);
            throw new StorageException(m, e);
        }
    }

    private String createFilename(String originalFilename) {
        String ext = com.google.common.io.Files.getFileExtension(originalFilename);
        return UUID.randomUUID().toString() + (Strings.isNullOrEmpty(ext) ? "" : "." + ext);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;

            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            rootLocation = Paths.get(rootFolder);
            log.debug("FileSystemStorageService rootFolder: {} rootLocation: {}", rootFolder, rootLocation);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize hotty", e);
        }
    }
}
