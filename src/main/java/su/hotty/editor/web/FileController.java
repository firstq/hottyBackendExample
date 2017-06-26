package su.hotty.editor.web;

import su.hotty.editor.dto.FileListResponse;
import su.hotty.editor.storage.StorageFileNotFoundException;
import su.hotty.editor.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import su.hotty.editor.config.API;

import java.io.IOException;
import java.util.stream.Collectors;


@RestController
@RequestMapping(API.PATH + "/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class.getSimpleName());

    private final StorageService storageService;

    @Value("${resources.images.folder}")
    private String imageFolder;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping()
    ResponseEntity<?> listUploadedFiles() throws IOException {
        log.debug("listUploadedFiles");

        FileListResponse fileListResponse = new FileListResponse(
                storageService
                        .loadAll()
                        .map(path -> MvcUriComponentsBuilder
                                .fromMethodName(FileController.class, "serveFile", path.getFileName().toString())
                                .build().toString())
                        .collect(Collectors.toList()));

        return ResponseEntity.ok(fileListResponse);
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        log.debug("serveFile filename:" + filename);
        Resource file = storageService.loadAsResource(imageFolder+filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping()
    ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String newFilename = storageService.store(file);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{filename}")
                        .buildAndExpand(newFilename)
                        .toUri()
        );

        return ResponseEntity.created(httpHeaders.getLocation()).body(new FileListResponse(newFilename));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException e) {
        log.error("Requested file not found.", e);
        return ResponseEntity.notFound().build();
    }

}
