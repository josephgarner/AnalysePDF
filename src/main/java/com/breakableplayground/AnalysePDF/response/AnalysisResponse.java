package com.breakableplayground.AnalysePDF.response;

import com.breakableplayground.AnalysePDF.domain.DocumentType;
import com.breakableplayground.AnalysePDF.domain.FontRatio;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnalysisResponse {
    private DocumentType documentType = DocumentType.UNDEFINED;
    private FontRatio fontRatio;
    private String error;

    public AnalysisResponse() {
    }

    public AnalysisResponse(String error) {
        this.error = error;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public FontRatio getFontRatio() {
        return fontRatio;
    }

    public void setFontRatio(FontRatio fontRatio) {
        this.fontRatio = fontRatio;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
