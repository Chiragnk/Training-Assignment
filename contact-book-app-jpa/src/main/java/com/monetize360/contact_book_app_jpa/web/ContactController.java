package com.monetize360.contact_book_app_jpa.web;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monetize360.contact_book_app_jpa.dto.ContactDTO;
import com.monetize360.contact_book_app_jpa.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/add")
    public ResponseEntity<ContactDTO> addContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO newContact = contactService.insertContact(contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newContact);
    }

    @PutMapping("/update")
    public ResponseEntity<ContactDTO> updateContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO updatedContact = contactService.updateContact(contactDTO);
        return ResponseEntity.ok(updatedContact);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Optional<ContactDTO>> getContact(@PathVariable("id") UUID id) {
        Optional<ContactDTO> contactDTO = contactService.getContactById(id);
        return ResponseEntity.ok(contactDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Optional<ContactDTO>> deleteContact(@PathVariable("id") UUID id) {
        contactService.deleteContact(id);
        Optional<ContactDTO> contactDTO = contactService.getContactById(id);
        return ResponseEntity.ok(contactDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactDTO>> getAllContacts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir) {
        List<ContactDTO> contacts = contactService.getAllContacts(page, size, sortBy, sortDir);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactDTO>> searchContacts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir,
            @RequestParam(value = "search", defaultValue = "") String search) {
        List<ContactDTO> contacts = contactService.searchContacts(page, size, sortBy, sortDir, search);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/qrcode/{id}")
    public ResponseEntity<BufferedImage> generateQRCodeById(@PathVariable("id") UUID id) {
        try {
            BufferedImage image = contactService.generateQRCodeById(id);
            if (image != null) {
                return new ResponseEntity<>(image, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
