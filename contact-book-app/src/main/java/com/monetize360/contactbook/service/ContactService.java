package com.monetize360.contactbook.service;

import com.monetize360.contactbook.dto.ContactDTO;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

public interface ContactService {
    ContactDTO createContact(ContactDTO contact) throws SQLException;
    boolean updateContact(ContactDTO contact) throws SQLException;
    boolean deleteContact(int contactId) throws SQLException;
    ContactDTO getContactById(int contactId) throws SQLException;
    List<ContactDTO> searchContacts(String keyword) throws SQLException;
    boolean importContactsFromExcel(InputStream inputStream);
    boolean exportContactsToExcel(OutputStream outputStream) throws SQLException;
}

