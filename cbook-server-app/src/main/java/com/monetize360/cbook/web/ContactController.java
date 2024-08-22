package com.monetize360.cbook.web;

import com.monetize360.cbook.domain.Contact;
import com.monetize360.cbook.dto.ContactDTO;
import com.monetize360.cbook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/add")
    public ResponseEntity<?> addContact(@RequestBody ContactDTO contactDTO) {
        try {
            ContactDTO newContact = contactService.insertContact(contactDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newContact);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add contact: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateContact(@RequestBody ContactDTO contactDTO) {
        try {
            ContactDTO updatedContact = contactService.updateContact(contactDTO);
            return ResponseEntity.ok(updatedContact);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update contact: " + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getContact(@PathVariable("id") UUID id) {
        try {
            ContactDTO contactDTO = contactService.getContactById(id.toString());
            return ResponseEntity.ok(contactDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact not found: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable("id") UUID id) {
        try {
            ContactDTO contact = contactService.getContactById(id.toString());
            contactService.deleteContact(id);
            contact.setDeleted(true);
            return ResponseEntity.ok(contact);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete contact: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllContacts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir) {
        try {
            List<ContactDTO> contacts = contactService.getAllContacts(page, size, sortBy, sortDir);
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch contacts: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchContacts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir,
            @RequestParam(value = "search", defaultValue = "") String search) {
        try {
            List<ContactDTO> contacts = contactService.searchContacts(page, size, sortBy, sortDir, search);
            return ResponseEntity.ok(contacts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search contacts: " + e.getMessage());
        }
    }
}
