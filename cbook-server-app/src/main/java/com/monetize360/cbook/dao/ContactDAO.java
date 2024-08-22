package com.monetize360.cbook.dao;

import com.monetize360.cbook.domain.Contact;
import com.monetize360.cbook.dto.ContactDTO;

import java.util.List;
import java.util.UUID;

public interface ContactDAO {
    Contact insertContact(Contact contact);
    Contact updateContact(Contact contact);
    Contact getContactById(String id);
    void deleteContact(UUID id);
    List<Contact> getAllContacts(int page, int size, String sortBy, String sortDir);
    List<Contact> searchContacts(int page, int size, String sortBy, String sortDir, String search);
}
