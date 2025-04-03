package com.breakableplayground.AnalysePDF.dao;

import com.breakableplayground.AnalysePDF.domain.PDFAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ReadPdfDAO {
    PDFAttributes extractPDFData(MultipartFile file) throws Exception;
    Map<String, String> extractCharacterFont(MultipartFile file) throws Exception;
}
