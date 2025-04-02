package com.breakableplayground.AnalysePDF.service.impl;

import com.breakableplayground.AnalysePDF.service.ContentScoringService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContentContentScoringServiceImpl implements ContentScoringService {

    private static final Map<String, Double> INVOICE_PATTERNS = Map.ofEntries(
            Map.entry("invoice", 0.5),
            Map.entry("due date", 0.3),
            Map.entry("payment terms", 0.2),
            Map.entry("bill to", 0.3),
            Map.entry("invoice number", 0.4),
            Map.entry("invoice date", 0.3),
            Map.entry("total amount", 0.2),
            Map.entry("tax", 0.1),
            Map.entry("payment information", 0.2),
            Map.entry("account number", 0.1),
            Map.entry("bsb", 0.1),
            Map.entry("item description", 0.1),
            Map.entry("quantity", 0.1),
            Map.entry("unit price", 0.1),
            Map.entry("subtotal", 0.1),
            Map.entry("amount due", 0.3)
    );

    private static final Map<String, Double> BANK_STATEMENT_PATTERNS = Map.ofEntries(
            Map.entry("account statement", 0.5),
            Map.entry("bank statement", 0.5),
            Map.entry("statement period", 0.3),
            Map.entry("opening balance", 0.4),
            Map.entry("closing balance", 0.4),
            Map.entry("transaction", 0.2),
            Map.entry("withdrawal", 0.3),
            Map.entry("deposit", 0.3),
            Map.entry("credit", 0.2),
            Map.entry("debit", 0.2),
            Map.entry("account number", 0.2),
            Map.entry("bsb", 0.1),
            Map.entry("available balance", 0.2),
            Map.entry("date", 0.1),
            Map.entry("description", 0.1),
            Map.entry("transaction date", 0.2)
    );

    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{1,2}[/.-]\\d{1,2}[/.-]\\d{2,4}");

    public double calculateInvoiceScore(List<String> lines){
        double initialScore = calculateScore(lines, INVOICE_PATTERNS);

        double additionalScore = 0.0;
        int dateCount = 0;

        for (String line : lines) {
            String normalizedLine = line.toLowerCase().trim();

            Matcher dateMatcher = DATE_PATTERN.matcher(normalizedLine);
            while (dateMatcher.find()) {
                dateCount++;
            }
        }

        if (dateCount > 0 && dateCount < 5) {
            additionalScore += 0.1;
        }

        return initialScore + additionalScore;
    }

    public double calculateStatementScore(List<String> lines){
        double initialScore = calculateScore(lines, BANK_STATEMENT_PATTERNS);

        double additionalScore = 0.0;
        int dateCount = 0;

        for (String line : lines) {
            String normalizedLine = line.toLowerCase().trim();


            Matcher dateMatcher = DATE_PATTERN.matcher(normalizedLine);
            while (dateMatcher.find()) {
                dateCount++;
            }
        }

        if (dateCount > 5) {
            additionalScore += 0.1 * Math.min(1.0, (dateCount - 5) / 10.0);
        }

        return initialScore + additionalScore;
    }

    private double calculateScore(List<String> lines, Map<String, Double> patterns){
        double score = 0.0;
        Map<String, Integer> matchCounts = new HashMap<>();

        patterns.keySet().forEach(pattern -> matchCounts.put(pattern, 0));

        for (String line : lines) {
            String normalizedLine = line.toLowerCase().trim();
            for (String pattern : patterns.keySet()) {
                if (normalizedLine.contains(pattern.toLowerCase())) {
                    matchCounts.put(pattern, matchCounts.get(pattern) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : matchCounts.entrySet()) {
            String pattern = entry.getKey();
            int count = entry.getValue();

            double patternScore = count > 0 ?
                    patterns.get(pattern) * (1 + Math.log(count) / Math.log(9)) : 0;
            score += patternScore;
        }

        return score;
    }
}
