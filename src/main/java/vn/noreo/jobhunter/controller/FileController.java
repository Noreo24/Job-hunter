package vn.noreo.jobhunter.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.noreo.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.noreo.jobhunter.service.FileService;
import vn.noreo.jobhunter.util.annotation.ApiMessage;
import vn.noreo.jobhunter.util.error.UploadFileException;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder)
            throws URISyntaxException, IOException, UploadFileException {

        // Validate
        if (file == null || file.isEmpty()) {
            throw new UploadFileException("File is empty, please select a file to upload.");
        }

        // Create the folder (directory) if it does not exist
        this.fileService.createDirectory(baseURI + folder);

        // Save the file to the folder
        String uploadFile = this.fileService.saveFile(file, folder, baseURI);

        ResUploadFileDTO resUploadFileDTO = new ResUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.ok().body(resUploadFileDTO);
    }
}
