package com.monetize360.invoicegeneration.service;

import com.monetize360.invoicegeneration.dto.MenuDTO;
import com.monetize360.invoicegeneration.dto.OrderDetailsDTO;
import com.monetize360.invoicegeneration.dto.DrinkDTO;
import com.monetize360.invoicegeneration.dto.DessertDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderService {
    private String customerName;
    private final OrderDetailsService orderDetailsService;

    public OrderService() {
        this.orderDetailsService = new OrderDetailsService();
    }

    public List<OrderDetailsDTO> processOrder(MenuDTO menuDTO) {
        List<OrderDetailsDTO> selectedItems = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the customer name:");
        customerName = scanner.nextLine();

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
                        orderDetailsService.calculateAmounts(item); // Calculate amounts for existing item
                        itemExists = true;
                        break;
                    }
                }

                if (!itemExists) {
                    orderDetailsService.calculateAmounts(orderDetails); // Calculate amounts for new item
                    selectedItems.add(orderDetails);
                }

                System.out.println("Added " + dishName + " to your order.");
            } else {
                System.out.println("Item not found.");
            }
        }
        return selectedItems;
    }

    private OrderDetailsDTO findItem(MenuDTO menuDTO, String dishName) {
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

    public String getCustomerName() {
        return customerName;
    }
}
