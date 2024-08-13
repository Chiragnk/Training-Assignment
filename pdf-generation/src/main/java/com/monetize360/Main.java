package com.monetize360;

import com.monetize360.pdfgeneration.service.PdfGenerationService;
import org.apache.fop.apps.FOPException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        PdfGenerationService pdfService = new PdfGenerationService();
        try {
            pdfService.createPdf();
            System.out.println("PDF generated successfully.");
        } catch (IOException | FOPException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
    }
