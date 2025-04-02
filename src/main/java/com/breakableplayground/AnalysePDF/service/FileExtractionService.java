package com.breakableplayground.AnalysePDF.service;

import com.breakableplayground.AnalysePDF.domain.PDFAttributes;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
public interface FileExtractionService {
    PDFAttributes extractPDFData(PDDocument document) throws IOException;

}
