package com.epam.training.gen.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PdfHelperService {

    @Value("${app.pdf.page-number-to-concatenate}")
    private int pageNumberToConcatenate;

    public List<String> extractTextFromPdf(PDDocument pdfFile) {
        val stripper = new PDFTextStripper();
        val texts = IntStream.rangeClosed(1, pdfFile.getNumberOfPages())
                .filter(pageNumber -> pageNumberToConcatenate < 2 || pageNumber % pageNumberToConcatenate == 1)
                .mapToObj(pageNumber -> {
                    stripper.setStartPage(pageNumber);
                    stripper.setEndPage(pageNumber + pageNumberToConcatenate - 1);
                    try {
                        return stripper.getText(pdfFile).replace("\n", " ").replaceAll("\\s{2,}", " ");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).filter(text -> text != null && !text.isBlank()).toList();
        if (texts.isEmpty()) {
            throw new IllegalStateException("No text found in the PDF file");
        }
        return texts;
    }
}
