package com.monetize360.pdfgeneration.service;

import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PdfGenerationService {
    public void createPdf() throws IOException, FOPException {
        File xmlFile = new File("C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\pdf-generation\\src\\main\\resources\\input.xml");
        File xsltFile = new File("C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\pdf-generation\\src\\main\\resources\\stylesheet.xsl");
        File pdfFile = new File("C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\pdf-generation\\src\\main\\resources\\output.pdf");

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        try (OutputStream out = new FileOutputStream(pdfFile)) {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Source src = new StreamSource(xmlFile);
            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(src, res);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}

