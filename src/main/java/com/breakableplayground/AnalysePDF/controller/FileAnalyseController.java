package com.breakableplayground.AnalysePDF.controller;

import com.breakableplayground.AnalysePDF.response.AnalysisResponse;
import com.breakableplayground.AnalysePDF.service.FileAnalysationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api/pdf")
public class FileAnalyseController {

    private final FileAnalysationService fileAnalysationService;

    @Autowired
    public FileAnalyseController(FileAnalysationService fileAnalysationService){
        this.fileAnalysationService = fileAnalysationService;
    }

    @PostMapping(value = "/analyse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnalysisResponse> uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.badRequest().body(new AnalysisResponse("Only PDF files are accepted"));
        }
        try{
            AnalysisResponse response = fileAnalysationService.AnalyseUploadedPDF(file);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(new AnalysisResponse(e.getMessage()));
        }
    }
}
