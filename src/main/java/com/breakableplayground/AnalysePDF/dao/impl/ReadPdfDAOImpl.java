package com.breakableplayground.AnalysePDF.dao.impl;

import com.breakableplayground.AnalysePDF.dao.ReadPdfDAO;
import com.breakableplayground.AnalysePDF.domain.PDFAttributes;
import com.breakableplayground.AnalysePDF.domain.PDFFontStripper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ReadPdfDAOImpl implements ReadPdfDAO {
    private static final Logger log = LoggerFactory.getLogger(ReadPdfDAOImpl.class);

    @Override
    public PDFAttributes extractPDFData(MultipartFile file) throws Exception {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            log.info("Extracting data from uploaded PDF");
            PDDocumentInformation attributes = document.getDocumentInformation();

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(1);
            stripper.setEndPage(document.getNumberOfPages());
            String extractedText = stripper.getText(document);

            return new PDFAttributes(
                    attributes.getCreationDate(),
                    attributes.getModificationDate(),
                    Arrays.stream(extractedText.split("\n")).toList());
        } catch (IOException e) {
            log.error("Failure to extract PDF attributes {}", e.getMessage());
            throw new Exception("Error unable to handle PDF file");
        }
    }

    public Map<String, String> extractCharacterFont(MultipartFile file) throws Exception {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFFontStripper stripper = new PDFFontStripper();
            stripper.setSortByPosition(true);
            Map<String, String> characterFont = new HashMap<>();

            for (int pageIndex = 1; pageIndex <= document.getNumberOfPages(); pageIndex++) {
                stripper.setStartPage(pageIndex);
                stripper.setEndPage(pageIndex);

                stripper.getText(document);
                stripper.myGetCharactersByArticle().forEach(article -> article.forEach(
                        character -> characterFont.put(character.getUnicode(), character.getFont().getName())
                ));
            }
            return characterFont;
        } catch (IOException e) {
            log.error("Failure extract fonts from file {}", e.getMessage());
            throw new Exception("Error unable to handle PDF file");
        }
    }
}
