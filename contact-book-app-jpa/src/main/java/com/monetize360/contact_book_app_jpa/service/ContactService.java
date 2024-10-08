package com.monetize360.contact_book_app_jpa.service;

import com.google.zxing.WriterException;
import com.monetize360.contact_book_app_jpa.dto.ContactDTO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactService {
    ContactDTO insertContact(ContactDTO contactDTO);
    ContactDTO updateContact(ContactDTO contactDTO);
    Optional<ContactDTO> getContactById(UUID id);
    void deleteContact(UUID id);
    List<ContactDTO> getAllContacts(int page, int size, String sortBy, String sortDir);
    List<ContactDTO> searchContacts(int page, int size, String sortBy, String sortDir, String search);

    BufferedImage generateQRCodeById(UUID id) throws IOException, WriterException;
}
