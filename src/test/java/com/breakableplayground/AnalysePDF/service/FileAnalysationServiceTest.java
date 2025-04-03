package com.breakableplayground.AnalysePDF.service;

import com.breakableplayground.AnalysePDF.dao.ReadPdfDAO;
import com.breakableplayground.AnalysePDF.domain.DocumentType;
import com.breakableplayground.AnalysePDF.domain.Font;
import com.breakableplayground.AnalysePDF.domain.FontRatio;
import com.breakableplayground.AnalysePDF.domain.PDFAttributes;
import com.breakableplayground.AnalysePDF.response.AnalysisResponse;
import com.breakableplayground.AnalysePDF.service.impl.FileAnalysationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileAnalysationServiceTest {

    @Mock
    private ReadPdfDAO readPdfDAO;

    @Mock
    private ContentScoringService contentScoringService;

    @Mock
    private MultipartFile mockFile;

    private FileAnalysationServiceImpl fileAnalysationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileAnalysationService = new FileAnalysationServiceImpl(readPdfDAO, contentScoringService);
    }

    @Test
    @DisplayName("Should determine file as Invoice when invoice score is higher then the threshold")
    void shouldDetermineInvoice() throws Exception {

        PDFAttributes pdfAttributes = new PDFAttributes(
                Calendar.getInstance(),
                Calendar.getInstance(),
                List.of("Invoice content")
        );

        Map<String, String> fontMap = new HashMap<>();
        fontMap.put("a", "Arial");
        fontMap.put("b", "Arial");
        fontMap.put("c", "Courier");
        fontMap.put("d", "Times New Roman");

        when(readPdfDAO.extractPDFData(mockFile)).thenReturn(pdfAttributes);
        when(readPdfDAO.extractCharacterFont(mockFile)).thenReturn(fontMap);
        when(contentScoringService.calculateInvoiceScore(any())).thenReturn(1.2);
        when(contentScoringService.calculateStatementScore(any())).thenReturn(0.4);

        AnalysisResponse response = fileAnalysationService.AnalyseUploadedPDF(mockFile);
        FontRatio fontRatio = response.getFontRatio();

        assertEquals(DocumentType.INVOICE, response.getDocumentType());

        List<Font> expectedFontRatios = new ArrayList<>();
        expectedFontRatios.add(new Font("Arial", 50.0));
        expectedFontRatios.add(new Font("Courier", 25.0));
        expectedFontRatios.add(new Font("Times New Roman", 25.0));

        assertNotNull(fontRatio);
        assertTrue(fontRatio.getRatios().containsAll(expectedFontRatios));

        verify(readPdfDAO).extractPDFData(mockFile);
        verify(readPdfDAO).extractCharacterFont(mockFile);
        verify(contentScoringService).calculateInvoiceScore(List.of("Invoice content"));
        verify(contentScoringService).calculateStatementScore(List.of("Invoice content"));
    }
}
