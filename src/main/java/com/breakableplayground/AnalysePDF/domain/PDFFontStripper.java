package com.breakableplayground.AnalysePDF.domain;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.List;

public class PDFFontStripper extends PDFTextStripper {
    public PDFFontStripper() throws IOException {
        super();
    }

    public List<List<TextPosition>> myGetCharactersByArticle() {
        return getCharactersByArticle();
    }
}
