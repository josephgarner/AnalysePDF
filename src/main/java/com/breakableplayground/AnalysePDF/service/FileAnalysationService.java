package com.breakableplayground.AnalysePDF.service;

import com.breakableplayground.AnalysePDF.response.AnalysisResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileAnalysationService {
    AnalysisResponse AnalyseUploadedPDF(MultipartFile pdfFile) throws Exception;
}
