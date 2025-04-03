package com.breakableplayground.AnalysePDF.service.impl;

import com.breakableplayground.AnalysePDF.dao.ReadPdfDAO;
import com.breakableplayground.AnalysePDF.domain.DocumentType;
import com.breakableplayground.AnalysePDF.domain.FontRatio;
import com.breakableplayground.AnalysePDF.domain.PDFAttributes;
import com.breakableplayground.AnalysePDF.response.AnalysisResponse;
import com.breakableplayground.AnalysePDF.service.FileAnalysationService;
import com.breakableplayground.AnalysePDF.service.ContentScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class FileAnalysationServiceImpl implements FileAnalysationService {
    private final ReadPdfDAO readPdfDAO;

    private final ContentScoringService contentScoringService;

    private static final double CONFIDENT_THRESHOLD = 1.0;
    private static final double PROBABLE_THRESHOLD = 0.6;

    @Autowired
    public FileAnalysationServiceImpl(ReadPdfDAO readPdfDAO, ContentScoringService contentScoringService) {
        this.readPdfDAO = readPdfDAO;
        this.contentScoringService = contentScoringService;
    }

    @Override
    public AnalysisResponse AnalyseUploadedPDF(MultipartFile file) throws Exception {
        PDFAttributes pdfAttributes = readPdfDAO.extractPDFData(file);
        Map<String, String> fontMap = readPdfDAO.extractCharacterFont(file);

        AnalysisResponse response = new AnalysisResponse();

        pdfAttributes.setFontRatio(calculateFontRatio(fontMap));

        double invoiceScore = contentScoringService.calculateInvoiceScore(pdfAttributes.getContent());
        double statementScore = contentScoringService.calculateStatementScore(pdfAttributes.getContent());

        response.setFontRatio(pdfAttributes.getFontRatio());

        if (invoiceScore > CONFIDENT_THRESHOLD && invoiceScore > statementScore * (1 + PROBABLE_THRESHOLD)) {
            response.setDocumentType(DocumentType.INVOICE);
        } else if (statementScore > CONFIDENT_THRESHOLD && statementScore > invoiceScore * (1 + PROBABLE_THRESHOLD)) {
            response.setDocumentType(DocumentType.STATEMENT);
        }
        return response;

    }

    private FontRatio calculateFontRatio(Map<String, String> characterList){
        Map<String, Integer> fontMap = new HashMap<>();
        for(String font : characterList.values()){
            fontMap.merge(font, 1, Integer::sum);
        }
        return new FontRatio(fontMap);
    }
}
