package org.example.controller;

import lombok.extern.log4j.Log4j;
import org.example.entity.AppDocument;
import org.example.entity.AppPhoto;
import org.example.entity.BinaryContent;
import org.example.service.FileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RequestMapping("/file")
@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-doc")
    public ResponseEntity<?> getDoc(@RequestParam("id") String id) {
        AppDocument appDocument = fileService.getDocument(id);
        if (appDocument == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = appDocument.getBinaryContent();

        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(appDocument.getMimeType()))
                .header("Content disposition", "attachment; filename=" + appDocument.getDocName())
                .body(fileSystemResource);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get-photo")
    public ResponseEntity<?> getPhoto(@RequestParam("id") String id) {
        AppPhoto appPhoto = fileService.getPhoto(id);
        if (appPhoto == null) {
            return ResponseEntity.badRequest().build();
        }
        BinaryContent binaryContent = appPhoto.getBinaryContent();

        FileSystemResource fileSystemResource = fileService.getFileSystemResource(binaryContent);
        if (fileSystemResource == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Content disposition", "attachment;")
                .body(fileSystemResource);
    }
}
