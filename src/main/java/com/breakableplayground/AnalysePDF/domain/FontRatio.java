package com.breakableplayground.AnalysePDF.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FontRatio {
    private final List<Font> ratios;

    public FontRatio(Map<String, Integer> ratios){
        double total = 0;
        for (int subTotal : ratios.values()) {
            total += subTotal;
        }
        List<Font> fontRatios = new ArrayList<>();
        for (var fontUsage: ratios.entrySet()){
            BigDecimal percentage = BigDecimal.valueOf(((fontUsage.getValue() / total) * 100));
            percentage = percentage.setScale(2, RoundingMode.HALF_UP);
            String fontName = fontUsage.getKey().substring(fontUsage.getKey().indexOf("+") + 1);
            fontRatios.add(new Font(fontName.trim(),  percentage.doubleValue()));
        }
        this.ratios = fontRatios;
    };

    public List<Font> getRatios() {
        return ratios;
    }
}
