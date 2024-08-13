package com.monetize360;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.monetize360.invoicegeneration.domain.Dessert;
import com.monetize360.invoicegeneration.domain.Drink;
import com.monetize360.invoicegeneration.domain.Menu;
import com.monetize360.invoicegeneration.dto.DessertDTO;
import com.monetize360.invoicegeneration.dto.DrinkDTO;
import com.monetize360.invoicegeneration.dto.MenuDTO;
import com.monetize360.invoicegeneration.dto.OrderDetailsDTO;
import com.monetize360.invoicegeneration.service.OrderDetailsService;
import com.monetize360.invoicegeneration.service.PdfGenerationService;
import org.apache.fop.apps.FOPException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String resourcesPath = "C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\invoice-generation\\src\\main\\resources";
        File jsonFile = new File(resourcesPath, "menu.json");
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();

        OrderDetailsService orderDetailsService = new OrderDetailsService();

        try {
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode menuNode = rootNode.path("menu");

            Menu menu = convertToMenu(menuNode);
            MenuDTO menuDTO = convertToMenuDTO(menu);

            displayMenuItems(menuDTO);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the customer name:");
            String customerName = scanner.nextLine();

            List<OrderDetailsDTO> selectedItems = new ArrayList<>();
            while (true) {
                System.out.println("\nEnter the name of the dish you want to order (or type 'exit' to finish):");
                String dishName = scanner.nextLine();

                if (dishName.equalsIgnoreCase("exit")) {
                    break;
                }

                OrderDetailsDTO orderDetails = findItem(menuDTO, dishName);

                if (orderDetails != null) {
                    boolean itemExists = false;
                    for (OrderDetailsDTO item : selectedItems) {
                        if (item.getName().equalsIgnoreCase(orderDetails.getName())) {
                            item.setQuantity(item.getQuantity() + 1);
                            orderDetailsService.calculateAmounts(item);
                            itemExists = true;
                            break;
                        }
                    }

                    if (!itemExists) {
                        orderDetailsService.calculateAmounts(orderDetails);
                        selectedItems.add(orderDetails);
                    }

                    System.out.println("Added " + dishName + " to your order.");
                } else {
                    System.out.println("Item not found.");
                }
            }

            // Generate XML content
            StringBuilder xmlContentBuilder = new StringBuilder("<menu>");
            xmlContentBuilder.append("<customerName>").append(customerName).append("</customerName>");
            for (OrderDetailsDTO item : selectedItems) {
                xmlContentBuilder.append(xmlMapper.writeValueAsString(item));
            }
            xmlContentBuilder.append("</menu>");
            String xmlContent = xmlContentBuilder.toString();
            File xmlFile = new File(resourcesPath, "menu.xml");
            try (FileOutputStream xmlOutputStream = new FileOutputStream(xmlFile)) {
                xmlOutputStream.write(xmlContent.getBytes());
            }

            double totalAmount = selectedItems.stream().mapToDouble(OrderDetailsDTO::getTotalAmount).sum();
            System.out.printf("Total Amount (including tax): %.2f\n", totalAmount);

            PdfGenerationService pdfGenerationService = new PdfGenerationService();
            pdfGenerationService.createPdf(xmlContent);
            System.out.println("PDF generated: " + new File(resourcesPath, "output.pdf").getAbsolutePath());

        } catch (IOException | FOPException e) {
            e.printStackTrace();
        }
    }

    private static Menu convertToMenu(JsonNode menuNode) {
        List<Dessert> desserts = new ArrayList<>();
        List<Drink> drinks = new ArrayList<>();

        JsonNode dessertsNode = menuNode.path("desserts");
        for (JsonNode node : dessertsNode) {
            String name = node.path("name").asText();
            double price = node.path("price").asDouble();
            int quantity = node.path("quantity").asInt(1);
            desserts.add(new Dessert(name, price, quantity));
        }

        JsonNode drinksNode = menuNode.path("drinks");
        for (JsonNode node : drinksNode) {
            String name = node.path("name").asText();
            double price = node.path("price").asDouble();
            int quantity = node.path("quantity").asInt(1);
            drinks.add(new Drink(name, price, quantity));
        }

        Menu menu = new Menu();
        menu.setDesserts(desserts);
        menu.setDrinks(drinks);
        return menu;
    }

    private static MenuDTO convertToMenuDTO(Menu menu) {
        List<DessertDTO> dessertDTOs = menu.getDesserts().stream()
                .map(d -> new DessertDTO(d.getName(), d.getPrice()))
                .collect(Collectors.toList());

        List<DrinkDTO> drinkDTOs = menu.getDrinks().stream()
                .map(d -> new DrinkDTO(d.getName(), d.getPrice()))
                .collect(Collectors.toList());

        return new MenuDTO(dessertDTOs, drinkDTOs);
    }

    private static void displayMenuItems(MenuDTO menuDTO) {
        System.out.println("Available Desserts:");
        for (DessertDTO dessert : menuDTO.getDesserts()) {
            System.out.println("- " + dessert.getName() + " ($" + dessert.getPrice() + ")");
        }

        System.out.println("Available Drinks:");
        for (DrinkDTO drink : menuDTO.getDrinks()) {
            System.out.println("- " + drink.getName() + " ($" + drink.getPrice() + ")");
        }
    }

    private static OrderDetailsDTO findItem(MenuDTO menuDTO, String dishName) {
        for (DessertDTO dessert : menuDTO.getDesserts()) {
            if (dessert.getName().equalsIgnoreCase(dishName)) {
                return new OrderDetailsDTO(dessert.getName(), dessert.getPrice(), 1, 5);
            }
        }

        for (DrinkDTO drink : menuDTO.getDrinks()) {
            if (drink.getName().equalsIgnoreCase(dishName)) {
                return new OrderDetailsDTO(drink.getName(), drink.getPrice(), 1, 5);
            }
        }

        return null;
    }
}
