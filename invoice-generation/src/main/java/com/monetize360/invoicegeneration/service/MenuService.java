package com.monetize360.invoicegeneration.service;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MenuService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public Menu loadMenu(File jsonFile) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        JsonNode menuNode = rootNode.path("menu");
        return convertToMenu(menuNode);
    }

    private Menu convertToMenu(JsonNode menuNode) {
        List<Dessert> desserts = new ArrayList<>();
        List<Drink> drinks = new ArrayList<>();

        JsonNode dessertsNode = menuNode.path("desserts");
        for (JsonNode node : dessertsNode) {
            String name = node.path("name").asText();
            double price = node.path("price").asDouble();
            int quantity = node.path("quantity").asInt(1); // Default quantity to 1 if not provided
            desserts.add(new Dessert(name, price, quantity));
        }

        JsonNode drinksNode = menuNode.path("drinks");
        for (JsonNode node : drinksNode) {
            String name = node.path("name").asText();
            double price = node.path("price").asDouble();
            int quantity = node.path("quantity").asInt(1); // Default quantity to 1 if not provided
            drinks.add(new Drink(name, price, quantity));
        }

        Menu menu = new Menu();
        menu.setDesserts(desserts);
        menu.setDrinks(drinks);
        return menu;
    }


    public MenuDTO convertToMenuDTO(Menu menu) {
        List<DessertDTO> dessertDTOs = menu.getDesserts().stream()
                .map(d -> new DessertDTO(d.getName(), d.getPrice()))
                .collect(Collectors.toList());

        List<DrinkDTO> drinkDTOs = menu.getDrinks().stream()
                .map(d -> new DrinkDTO(d.getName(), d.getPrice()))
                .collect(Collectors.toList());

        return new MenuDTO(dessertDTOs, drinkDTOs);
    }

    public void saveMenuToXml(String xmlContent, File xmlFile) throws IOException {
        try (FileOutputStream xmlOutputStream = new FileOutputStream(xmlFile)) {
            xmlOutputStream.write(xmlContent.getBytes());
        }
    }

    public String createXmlContent(String customerName, List<OrderDetailsDTO> selectedItems) throws IOException {
        StringBuilder xmlContentBuilder = new StringBuilder("<menu>");
        xmlContentBuilder.append("<customerName>").append(customerName).append("</customerName>");
        for (OrderDetailsDTO item : selectedItems) {
            xmlContentBuilder.append(xmlMapper.writeValueAsString(item));
        }
        xmlContentBuilder.append("</menu>");
        return xmlContentBuilder.toString();
    }

    public void displayMenuItems(MenuDTO menuDTO) {
        System.out.println("Available Desserts:");
        for (DessertDTO dessert : menuDTO.getDesserts()) {
            System.out.println("- " + dessert.getName() + " ($" + dessert.getPrice() + ")");
        }

        System.out.println("Available Drinks:");
        for (DrinkDTO drink : menuDTO.getDrinks()) {
            System.out.println("- " + drink.getName() + " ($" + drink.getPrice() + ")");
        }
    }
}
