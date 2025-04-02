package com.breakableplayground.AnalysePDF.domain;

import java.util.Calendar;
import java.util.List;

public record PDFAttributes(
        Calendar creationDate,
        Calendar modDate,
        List<String> content
) { }
