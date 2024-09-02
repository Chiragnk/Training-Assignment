package com.monetize360.contact_book_application.web;

import com.monetize360.contact_book_application.dto.ContactDTO;
import com.monetize360.contact_book_application.service.ContactService;
import com.monetize360.contact_book_application.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;
    @Autowired
    private JwtUtil jwtUtil;
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
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "firstName") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir) {

        logger.info("Received request for /all endpoint with Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("Extracted token: {}", token);

            String username = jwtUtil.getUsernameFromToken(token);
            logger.info("Extracted username from token: {}", username);

            if (jwtUtil.validateToken(token, username)) {
                logger.info("Token is valid. Fetching contacts...");
                List<ContactDTO> contacts = contactService.getAllContacts(page, size, sortBy, sortDir);
                return ResponseEntity.ok(contacts);
            } else {
                logger.warn("Invalid token: {}", token);
            }
        } else {
            logger.warn("Authorization header is missing or does not start with 'Bearer '");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
//    @PostMapping("/import")
//    public ResponseEntity<String> importContacts(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
//        }
//        try {
//            contactService.importContacts(file);
//            return new ResponseEntity<>("Contacts imported successfully", HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>("Failed to import contacts", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
@PostMapping("/import/{id}")
public String importContacts(@RequestParam("file") MultipartFile file, @PathVariable("id") UUID userId) {
    try {
        contactService.importContacts(userId, file);
        return "Contacts imported successfully for user ID: " + userId;
    } catch (Exception e) {
        return "Failed to import contacts: " + e.getMessage();
    }
}
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportContacts() {
        try {
            byte[] excelFile = contactService.exportContacts();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "output.xlsx");
            return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/import/csv")
    public ResponseEntity<String> importContactsFromCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);
        }
        try {
            contactService.importContactsFromCSV(file);
            return new ResponseEntity<>("Contacts imported successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportContactsToCSV() {
        try {
            byte[] csvFile = contactService.exportContactsToCSV();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "contacts.csv");
            return new ResponseEntity<>(csvFile, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<String> shareContactByEmail(@PathVariable UUID id, @RequestParam String email) {
        try {
            contactService.shareContactByEmail(id, email);
            return ResponseEntity.ok("Contact shared successfully via email.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to share contact.");
        }
    }
}


