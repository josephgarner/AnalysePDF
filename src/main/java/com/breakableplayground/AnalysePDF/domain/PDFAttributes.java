package com.breakableplayground.AnalysePDF.domain;

import java.util.Calendar;
import java.util.List;

public class PDFAttributes {
    private final Calendar creationDate;
    private final Calendar modDate;
    private FontRatio fontRatio;
    private final List<String> content;

    public PDFAttributes(Calendar creationDate, Calendar modDate, List<String> content){
        this.creationDate = creationDate;
        this.modDate = modDate;
        this.content = content;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public Calendar getModDate() {
        return modDate;
    }

    public FontRatio getFontRatio() {
        return fontRatio;
    }

    public void setFontRatio(FontRatio fontRatio) {
        this.fontRatio = fontRatio;
    }

    public List<String> getContent() {
        return content;
    }
}
