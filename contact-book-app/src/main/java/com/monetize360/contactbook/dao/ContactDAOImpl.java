package com.monetize360.contactbook.dao;

import com.monetize360.contactbook.service.DatabaseReaderUtil;
import com.monetize360.contactbook.domain.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {

    @Override
    public Contact createContact(Contact contact) throws SQLException {
        String query = "INSERT INTO contacts (name, email, dob, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseReaderUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getEmail());
            stmt.setDate(3, contact.getDob() != null ? Date.valueOf(contact.getDob()) : null);
            stmt.setString(4, contact.getPhone());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getInt(1));
                }
            }
        }
        return contact;
    }

    @Override
    public boolean updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contacts SET name = ?, email = ?, dob = ?, phone = ? WHERE id = ?";
        try (Connection conn = DatabaseReaderUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getEmail());
            stmt.setDate(3, contact.getDob() != null ? Date.valueOf(contact.getDob()) : null);
            stmt.setString(4, contact.getPhone());
            stmt.setInt(5, contact.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteContact(int contactId) throws SQLException {
        String query = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DatabaseReaderUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contactId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Contact getContactById(int contactId) throws SQLException {
        String query = "SELECT * FROM contacts WHERE id = ?";
        try (Connection conn = DatabaseReaderUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contactId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Contact contact = new Contact();
                    contact.setId(rs.getInt("id"));
                    contact.setName(rs.getString("name"));
                    contact.setEmail(rs.getString("email"));
                    contact.setDob(rs.getDate("dob").toString());
                    contact.setPhone(rs.getString("phone"));
                    return contact;
                }
            }
        }
        return null;
    }

    @Override
    public List<Contact> searchContacts(String keyword) throws SQLException {
        String query = "SELECT * FROM contacts WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";
        List<Contact> contacts = new ArrayList<>();
        try (Connection conn = DatabaseReaderUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Contact contact = new Contact();
                    contact.setId(rs.getInt("id"));
                    contact.setName(rs.getString("name"));
                    contact.setEmail(rs.getString("email"));
                    contact.setDob(rs.getDate("dob").toString());
                    contact.setPhone(rs.getString("phone"));
                    contacts.add(contact);
                }
            }
        }
        return contacts;
    }

    @Override
    public List<Contact> getAllContacts() throws SQLException {
        String query = "SELECT * FROM contacts";
        List<Contact> contacts = new ArrayList<>();
        try (Connection conn = DatabaseReaderUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setName(rs.getString("name"));
                contact.setEmail(rs.getString("email"));
                contact.setDob(rs.getDate("dob").toString());
                contact.setPhone(rs.getString("phone"));
                contacts.add(contact);
            }
        }
        return contacts;
    }
}

