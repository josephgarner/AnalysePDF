package com.breakableplayground.AnalysePDF.service;

import java.util.List;

public interface ContentScoringService {
    double calculateInvoiceScore(List<String> lines);

    double calculateStatementScore(List<String> lines);
}
