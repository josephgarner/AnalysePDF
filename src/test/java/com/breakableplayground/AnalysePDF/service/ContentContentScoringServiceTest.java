package com.breakableplayground.AnalysePDF.service;

import com.breakableplayground.AnalysePDF.service.impl.ContentContentScoringServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContentContentScoringServiceTest {

    private ContentContentScoringServiceImpl scoringService;

    @BeforeEach
    void setUp() {
        scoringService = new ContentContentScoringServiceImpl();
    }

    @Test
    @DisplayName("Should return high invoice score for invoice content")
    public void shouldReturnHighInvoiceScore(){
        List<String> invoiceLines = Arrays.asList(
                "INVOICE",
                "Invoice Number: INV-12345",
                "Invoice Date: 15/03/2025",
                "Due Date: 15/04/2025",
                "Bill To: John Doe",
                "Payment Terms: Net 30",
                "Item Description: Web Development Services",
                "Quantity: 1",
                "Unit Price: $1000.00",
                "Subtotal: $1000.00",
                "Tax: $100.00",
                "Total Amount: $1100.00",
                "Amount Due: $1100.00",
                "Payment Information:",
                "Account Number: 123456789",
                "BSB: 123-456"
        );

        double invoiceScore = scoringService.calculateInvoiceScore(invoiceLines);
        double statementScore = scoringService.calculateStatementScore(invoiceLines);

        assertTrue(invoiceScore > 3.0);
        assertTrue(invoiceScore > statementScore);
    }

    @Test
    @DisplayName("Should return high statement score for bank statement content")
    void shouldReturnHighStatementScore() {
        List<String> statementLines = Arrays.asList(
                "BANK STATEMENT",
                "Account Statement",
                "Statement Period: 01/01/2025 - 31/01/2025",
                "Account Number: 987654321",
                "BSB: 789-012",
                "Opening Balance: $5000.00",
                "Closing Balance: $5500.00",
                "Available Balance: $5500.00",
                "Transaction Date: 05/01/2025",
                "Description: Salary Deposit",
                "Credit: $2000.00",
                "Transaction Date: 10/01/2025",
                "Description: Rent Payment",
                "Debit: $1500.00",
                "Transaction Date: 15/01/2025",
                "Description: Grocery Shopping",
                "Withdrawal: $200.00",
                "Transaction Date: 20/01/2025",
                "Description: Utility Bill",
                "Debit: $150.00",
                "Transaction Date: 25/01/2025",
                "Description: Interest",
                "Credit: $50.00"
        );

        double statementScore = scoringService.calculateStatementScore(statementLines);
        double invoiceScore = scoringService.calculateInvoiceScore(statementLines);

        assertTrue(statementScore > 3.0);
        assertTrue(statementScore > invoiceScore);
    }

    @Test
    @DisplayName("Should return low scores for unrelated content")
    void shouldReturnLowScoresForUnrelatedContent() {
        List<String> unrelatedLines = Arrays.asList(
                "Taylor went to the store",
                "Bikes ride up hill",
                "It is 23 today",
                "How could we?"
        );

        double invoiceScore = scoringService.calculateInvoiceScore(unrelatedLines);
        double statementScore = scoringService.calculateStatementScore(unrelatedLines);

        assertTrue(invoiceScore < 0.5);
        assertTrue(statementScore < 0.5);
    }

    @Test
    @DisplayName("Should handle empty input")
    void shouldHandleEmptyInput() {
        List<String> emptyLines = List.of();

        double invoiceScore = scoringService.calculateInvoiceScore(emptyLines);
        double statementScore = scoringService.calculateStatementScore(emptyLines);

        assertEquals(0.0, invoiceScore);
        assertEquals(0.0, statementScore);
    }

    @Test
    @DisplayName("Should increment score for dates in invoice")
    void shouldIncrementScoreForDatesInInvoice() {
        List<String> invoiceWithDates = Arrays.asList(
                "Invoice",
                "Date: 01/02/2025",
                "Due Date: 15/02/2025"
        );

        List<String> invoiceWithoutDates = Arrays.asList(
                "Invoice",
                "Reference: 12345"
        );

        double scoreWithDates = scoringService.calculateInvoiceScore(invoiceWithDates);
        double scoreWithoutDates = scoringService.calculateInvoiceScore(invoiceWithoutDates);

        assertTrue(scoreWithDates > scoreWithoutDates);
    }

    @Test
    @DisplayName("Should increment score for multiple dates in statement")
    void shouldIncrementScoreForMultipleDatesInStatement() {
        List<String> statementWithManyDates = Arrays.asList(
                "01/01/2025",
                "05/01/2025",
                "10/01/2025",
                "15/01/2025",
                "20/01/2025",
                "25/01/2025",
                "30/01/2025"
        );

        List<String> statementWithFewDates = List.of(
                "01/01/2025"
        );

        double scoreWithManyDates = scoringService.calculateStatementScore(statementWithManyDates);
        double scoreWithFewDates = scoringService.calculateStatementScore(statementWithFewDates);

        assertTrue(scoreWithManyDates > scoreWithFewDates);
    }
}
