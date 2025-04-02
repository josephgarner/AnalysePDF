package com.breakableplayground.AnalysePDF.service.impl;

import com.breakableplayground.AnalysePDF.domain.PDFAttributes;
import com.breakableplayground.AnalysePDF.service.FileAnalysationService;
import com.breakableplayground.AnalysePDF.service.FileExtractionService;
import com.breakableplayground.AnalysePDF.service.ContentScoringService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileAnalysationServiceImpl implements FileAnalysationService {
    private static final Logger log = LoggerFactory.getLogger(FileAnalysationServiceImpl.class);

    private final FileExtractionService fileExtractionService;

    private final ContentScoringService contentScoringService;

    private static final double CONFIDENT_THRESHOLD = 1.0;
    private static final double PROBABLE_THRESHOLD = 0.6;

    @Autowired
    public FileAnalysationServiceImpl(FileExtractionService fileExtractionService, ContentScoringService contentScoringService) {
        this.fileExtractionService = fileExtractionService;
        this.contentScoringService = contentScoringService;
    }

    @Override
    public String AnalyseUploadedPDF(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            PDDocument document = PDDocument.load(inputStream);
            PDFAttributes pdfAttributes = fileExtractionService.extractPDFData(document);

            double invoiceScore = contentScoringService.calculateInvoiceScore(pdfAttributes.content());
            double statementScore = contentScoringService.calculateStatementScore(pdfAttributes.content());

            document.close();

            if (invoiceScore > CONFIDENT_THRESHOLD && invoiceScore > statementScore * (1 + PROBABLE_THRESHOLD)) {
                return "Invoice";
            } else if (statementScore > CONFIDENT_THRESHOLD && statementScore > invoiceScore * (1 + PROBABLE_THRESHOLD)) {
                return "Statement";
            } else {
                return "Unknown";
            }
        } catch (IOException ioe) {
            log.error("Error");
        }
        return "";
    }
}
