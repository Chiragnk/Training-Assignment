package com.monetize360.contact_book_application.service;
import com.google.zxing.WriterException;
import com.monetize360.contact_book_application.dto.ContactDTO;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactService  {
    ContactDTO insertContact(ContactDTO contactDTO);
    ContactDTO updateContact(ContactDTO contactDTO);
    Optional<ContactDTO> getContactById(UUID id);
    void deleteContact(UUID id);
    List<ContactDTO> getAllContacts(int page, int size, String sortBy, String sortDir);
    List<ContactDTO> searchContacts(int page, int size, String sortBy, String sortDir, String search);
    BufferedImage generateQRCodeById(UUID id) throws IOException, WriterException;
    //void importContacts(MultipartFile file);

    void importContacts(UUID userId, MultipartFile file);

    byte[] exportContacts();
    void importContactsFromCSV(MultipartFile file);
    byte[] exportContactsToCSV();
    void shareContactByEmail(UUID id, String recipientEmail) throws IOException, WriterException, MessagingException;
}
