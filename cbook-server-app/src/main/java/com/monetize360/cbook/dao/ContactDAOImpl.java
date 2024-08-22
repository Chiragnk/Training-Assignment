package com.monetize360.cbook.dao;

import com.monetize360.cbook.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Repository
public class ContactDAOImpl implements ContactDAO{
    @Autowired
private JdbcTemplate jdbcTemplate;
    @Override
    public Contact insertContact(Contact contact) {
        String sql = "INSERT INTO contact (id, firstName, lastName, email, phone, deleted) VALUES (?, ?, ?, ?, ?, ?)";
        UUID id = UUID.randomUUID();
        jdbcTemplate.update(sql, id, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone(), contact.isDeleted());
        contact.setId(id);
        return contact;
    }

    @Override
    public Contact updateContact(Contact contact) {
        String sql = "UPDATE contact SET firstName = ?, lastName = ?, email = ?, phone = ?, deleted = ? WHERE id = ?";
        jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone(), contact.isDeleted(), contact.getId());
        return contact;
    }

    @Override
    public Contact getContactById(String id) {
        String sql = "SELECT * FROM contact WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{UUID.fromString(id)}, (rs, rowNum) -> {
            Contact contact = new Contact();
            contact.setId(UUID.fromString(rs.getString("id")));
            contact.setFirstName(rs.getString("firstName"));
            contact.setLastName(rs.getString("lastName"));
            contact.setEmail(rs.getString("email"));
            contact.setPhone(rs.getString("phone"));
            contact.setDeleted(rs.getBoolean("deleted"));
            return contact;
        });
    }

    @Override
    public void deleteContact(UUID id) {
        String sql = "UPDATE contact SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Contact> getAllContacts(int page, int size, String sortBy, String sortDir) {
        String sql = "SELECT * FROM contact WHERE deleted = false";
        if (sortBy != null && !sortBy.isEmpty()) {
            sortDir = (sortDir != null && (sortDir.equalsIgnoreCase("ASC") || sortDir.equalsIgnoreCase("DESC"))) ? sortDir : "ASC";
            sql += " ORDER BY " + sortBy + " " + sortDir;
        }
        int offset = (page - 1) * size;
        sql += " LIMIT ? OFFSET ?";
        List<Object> params = new ArrayList<>();
        params.add(size);
        params.add(offset);
        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> {
            Contact contact = new Contact();
            contact.setId(UUID.fromString(rs.getString("id")));
            contact.setFirstName(rs.getString("firstName"));
            contact.setLastName(rs.getString("lastName"));
            contact.setEmail(rs.getString("email"));
            contact.setPhone(rs.getString("phone"));
            contact.setDeleted(rs.getBoolean("deleted"));
            return contact;
        });
    }


    @Override
    public List<Contact> searchContacts(int page, int size, String sortBy, String sortDir, String search) {
        String sql = "SELECT * FROM contact WHERE deleted = false";
        if (search != null && !search.isEmpty()) {
            sql += " AND (firstName LIKE ? OR lastName LIKE ? OR email LIKE ? OR phone LIKE ?)";
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            sortDir = (sortDir != null && (sortDir.equalsIgnoreCase("ASC") || sortDir.equalsIgnoreCase("DESC"))) ? sortDir : "ASC";
            sql += " ORDER BY " + sortBy + " " + sortDir;
        }
        int offset = (page - 1) * size;
        sql += " LIMIT ? OFFSET ?";
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            String searchPattern = "%" + search + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        params.add(size);
        params.add(offset);

        return jdbcTemplate.query(sql, params.toArray(), (rs, rowNum) -> {
            Contact contact = new Contact();
            contact.setId(UUID.fromString(rs.getString("id")));
            contact.setFirstName(rs.getString("firstName"));
            contact.setLastName(rs.getString("lastName"));
            contact.setEmail(rs.getString("email"));
            contact.setPhone(rs.getString("phone"));
            contact.setDeleted(rs.getBoolean("deleted"));
            return contact;
        });
    }
}
