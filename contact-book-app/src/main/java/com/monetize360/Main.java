package com.monetize360;

import com.monetize360.contactbook.dto.ContactDTO;
import com.monetize360.contactbook.service.ContactService;
import com.monetize360.contactbook.service.ContactServiceImpl;
import com.monetize360.contactbook.service.DatabaseReaderUtil;

import java.io.*;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ContactService contactService = new ContactServiceImpl();
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;

        while (flag) {
            System.out.println("\nContact Book Menu:");
            System.out.println("1. Create Contact");
            System.out.println("2. Update Contact");
            System.out.println("3. Delete Contact");
            System.out.println("4. Get Contact by ID");
            System.out.println("5. Search Contacts");
            System.out.println("6. Import Contacts from Excel");
            System.out.println("7. Export Contacts to Excel");
            System.out.println("8. Exit");

            System.out.print("Choose an option: ");
            int option;
            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }

            switch (option) {
                case 1:
                    ContactDTO newContact = new ContactDTO();
                    System.out.print("Enter Name: ");
                    newContact.setName(scanner.nextLine());
                    System.out.print("Enter Email: ");
                    newContact.setEmail(scanner.nextLine());
                    System.out.print("Enter DOB (YYYY-MM-DD): ");
                    newContact.setDob(scanner.nextLine());
                    System.out.print("Enter Mobile: ");
                    newContact.setPhone(scanner.nextLine());
                    ContactDTO createdContact = contactService.createContact(newContact);
                    System.out.println("Contact Created: " + createdContact.getId());
                    break;

                case 2:
                    System.out.print("Enter ID of contact to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    ContactDTO existingContact = contactService.getContactById(updateId);

                    if (existingContact == null) {
                        System.out.println("Contact not found.");
                        break;
                    }

                    System.out.println("\nWhich field do you want to update?");
                    System.out.println("1. Name");
                    System.out.println("2. Email");
                    System.out.println("3. DOB (yyyy-mm-dd)");
                    System.out.println("4. Phone");
                    System.out.print("Enter your choice: ");
                    String choice = scanner.nextLine();

                    if (choice.equals("1")) {
                        System.out.print("Enter New Name: ");
                        existingContact.setName(scanner.nextLine());
                    } else if (choice.equals("2")) {
                        System.out.print("Enter New Email: ");
                        existingContact.setEmail(scanner.nextLine());
                    } else if (choice.equals("3")) {
                        System.out.print("Enter New DOB (yyyy-mm-dd): ");
                        existingContact.setDob(scanner.nextLine());
                    } else if (choice.equals("4")) {
                        System.out.print("Enter New Phone: ");
                        existingContact.setPhone(scanner.nextLine());
                    } else {
                        System.out.println("Invalid choice, update canceled.");
                        break;
                    }
                    if (contactService.updateContact(existingContact)) {
                        System.out.println("Contact updated successfully.");
                    } else {
                        System.out.println("Failed to update contact.");
                    }
                    break;

                case 3:
                    System.out.print("Enter ID of contact to delete: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();
                    if (contactService.deleteContact(deleteId)) {
                        System.out.println("Contact deleted successfully.");
                    } else {
                        System.out.println("Failed to delete contact.");
                    }
                    break;

                case 4:
                    System.out.print("Enter ID of contact to retrieve: ");
                    int getId = scanner.nextInt();
                    scanner.nextLine();

                    ContactDTO contact = contactService.getContactById(getId);
                    if (contact != null) {
                        System.out.println("Contact Details:");
                        System.out.println("ID: " + contact.getId());
                        System.out.println("Name: " + contact.getName());
                        System.out.println("Email: " + contact.getEmail());
                        System.out.println("DOB: " + contact.getDob());
                        System.out.println("Phone: " + contact.getPhone());
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter search keyword: ");
                    String keyword = scanner.nextLine();

                    List<ContactDTO> contacts = contactService.searchContacts(keyword);
                    if (contacts.isEmpty()) {
                        System.out.println("No contacts found.");
                    } else {
                        System.out.println("Search Results:");
                        for (ContactDTO c : contacts) {
                            System.out.println("ID: " + c.getId() + ", Name: " + c.getName() +
                                    ", Email: " + c.getEmail() + ", DOB: " + c.getDob() +
                                    ", Phone: " + c.getPhone());
                        }
                    }
                    break;

                case 6: // Import Contacts from Excel
                    try (InputStream inputStream = new FileInputStream("C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\contact-book-app\\src\\main\\resources\\contact.xlsx")) {
                        boolean imported = contactService.importContactsFromExcel(inputStream);
                        if (imported) {
                            System.out.println("Contacts imported successfully.");
                        } else {
                            System.out.println("Failed to import contacts.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 7:
                   try (OutputStream outputStream = new FileOutputStream("C:\\Users\\chira\\IdeaProjects\\Training\\mtz-java-training\\contact-book-app\\src\\main\\resources\\example.xlsx")) {
                       boolean exported = contactService.exportContactsToExcel(outputStream);
                   if (exported) {
                    System.out.println("Contacts exported successfully.");
                   } else {
                    System.out.println("Failed to export contacts.");
                   }
                  } catch (Exception e) {
                   e.printStackTrace();
                  }
                   break;
                case 8:
                    flag = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
