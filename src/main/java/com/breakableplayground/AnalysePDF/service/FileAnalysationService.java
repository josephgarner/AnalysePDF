package com.breakableplayground.AnalysePDF.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileAnalysationService {
    String AnalyseUploadedPDF(MultipartFile pdfFile);
}
