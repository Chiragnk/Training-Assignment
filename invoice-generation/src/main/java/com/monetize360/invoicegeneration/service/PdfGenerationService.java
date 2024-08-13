package com.monetize360.invoicegeneration.service;

import org.apache.fop.apps.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class PdfGenerationService {
    public void createPdf(String xmlContent) throws IOException, FOPException {
        InputStream xsltInputStream = getClass().getClassLoader().getResourceAsStream("menu.xsl");
        File tempXmlFile = File.createTempFile("menu", ".xml");
        try (FileOutputStream xmlOutputStream = new FileOutputStream(tempXmlFile)) {
            xmlOutputStream.write(xmlContent.getBytes());
        }
        File pdfFile = new File("C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\invoice-generation\\src\\main\\resources\\output.pdf");
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        try (OutputStream out = new FileOutputStream(pdfFile)) {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltInputStream));
            Source src = new StreamSource(new FileInputStream(tempXmlFile));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } finally {
            tempXmlFile.delete();
        }
    }
}
