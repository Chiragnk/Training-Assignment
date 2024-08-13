package com.monetize360.contactbook.service;

import com.monetize360.contactbook.dto.ContactDTO;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

public interface ContactService {
    ContactDTO createContact(ContactDTO contact) throws SQLException;
    boolean updateContact(ContactDTO contact);
    boolean deleteContact(int contactId);
    ContactDTO getContactById(int contactId);
    List<ContactDTO> searchContacts(String keyword);
    boolean importContactsFromExcel(InputStream inputStream);
    boolean exportContactsToExcel(OutputStream outputStream) throws SQLException;
}

