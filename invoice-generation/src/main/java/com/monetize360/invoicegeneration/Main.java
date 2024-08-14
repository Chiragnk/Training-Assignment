package com.monetize360.invoicegeneration;

import com.monetize360.invoicegeneration.domain.Menu;
import com.monetize360.invoicegeneration.dto.MenuDTO;
import com.monetize360.invoicegeneration.dto.OrderDetailsDTO;
import com.monetize360.invoicegeneration.service.MenuService;
import com.monetize360.invoicegeneration.service.OrderService;
import com.monetize360.invoicegeneration.service.PdfGenerationService;
import org.apache.fop.apps.FOPException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String resourcesPath = "C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\invoice-generation\\src\\main\\resources";
        File jsonFile = new File(resourcesPath, "menu.json");
        File xmlFile = new File(resourcesPath, "menu.xml");

        MenuService menuService = new MenuService();
        OrderService orderService = new OrderService();
        PdfGenerationService pdfGenerationService = new PdfGenerationService();

        try {
            Menu menu = menuService.loadMenu(jsonFile);
            MenuDTO menuDTO = menuService.convertToMenuDTO(menu);
            menuService.displayMenuItems(menuDTO);
            List<OrderDetailsDTO> selectedItems = orderService.processOrder(menuDTO);

            String xmlContent = menuService.createXmlContent(orderService.getCustomerName(), selectedItems);
            menuService.saveMenuToXml(xmlContent, xmlFile);

            double totalAmount = selectedItems.stream().mapToDouble(OrderDetailsDTO::getTotalAmount).sum();
            System.out.printf("Total Amount (including tax): %.2f\n", totalAmount);

            pdfGenerationService.createPdf(xmlContent);
            System.out.println("PDF generated: " + new File(resourcesPath, "output.pdf").getAbsolutePath());

        } catch (IOException | FOPException e) {
            e.printStackTrace();
        }
    }
}
