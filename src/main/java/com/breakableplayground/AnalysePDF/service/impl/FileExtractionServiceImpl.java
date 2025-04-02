package com.breakableplayground.AnalysePDF.service.impl;

import com.breakableplayground.AnalysePDF.domain.PDFAttributes;
import com.breakableplayground.AnalysePDF.service.FileExtractionService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class FileExtractionServiceImpl implements FileExtractionService {

    private static final Logger log = LoggerFactory.getLogger(FileExtractionServiceImpl.class);

    @Override
    public PDFAttributes extractPDFData(PDDocument document) throws IOException {
        log.info("Extracting data from uploaded PDF");
        PDDocumentInformation attributes = document.getDocumentInformation();

        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(1);
        stripper.setEndPage(Math.min(5, document.getNumberOfPages()));
        String extractedText = stripper.getText(document);

        return new PDFAttributes(
                attributes.getCreationDate(),
                attributes.getModificationDate(),
                Arrays.stream(extractedText.split("\n")).toList());
    }
}
