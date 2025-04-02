package com.breakableplayground.AnalysePDF.controller;

import com.breakableplayground.AnalysePDF.service.FileAnalysationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/file")
public class FileAnalyseController {

    private final FileAnalysationService fileAnalysationService;

    @Autowired
    public FileAnalyseController(FileAnalysationService fileAnalysationService){
        this.fileAnalysationService = fileAnalysationService;
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files are supported");
        }

        String response = fileAnalysationService.AnalyseUploadedPDF(file);

        return ResponseEntity.ok(response);
    }
}
