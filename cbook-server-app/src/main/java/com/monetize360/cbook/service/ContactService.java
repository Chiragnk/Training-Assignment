package com.monetize360.cbook.service;

import com.monetize360.cbook.dto.ContactDTO;

import java.util.List;
import java.util.UUID;

public interface ContactService {
    ContactDTO insertContact(ContactDTO contactDTO);
    ContactDTO updateContact(ContactDTO contactDTO);
    ContactDTO getContactById(String id);
    void deleteContact(UUID id);
    List<ContactDTO> searchContacts(int page, int size, String sortBy, String sortDir, String search);
    List<ContactDTO> getAllContacts(int page, int size, String sortBy, String sortDir);
}
