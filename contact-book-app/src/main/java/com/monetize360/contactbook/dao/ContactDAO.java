package com.monetize360.contactbook.dao;

import com.monetize360.contactbook.domain.Contact;

import java.sql.SQLException;
import java.util.List;

public interface ContactDAO {
    Contact createContact(Contact contact) throws SQLException;
    boolean updateContact(Contact contact) throws SQLException;
    boolean deleteContact(int contactId) throws SQLException;
    Contact getContactById(int contactId) throws SQLException;
    List<Contact> searchContacts(String keyword) throws SQLException;
    List<Contact> getAllContacts() throws SQLException;
}

