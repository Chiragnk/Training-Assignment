import com.monetize360.contactbook.dao.ContactDAO;
import com.monetize360.contactbook.dao.ContactDAOImpl;
import com.monetize360.contactbook.domain.Contact;
import com.monetize360.contactbook.service.DatabaseReaderUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactDAOTest {

    private ContactDAO contactDAO;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseReaderUtil.useTestSchema();

        contactDAO = new ContactDAOImpl();
        try (Connection conn = DatabaseReaderUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE contacts RESTART IDENTITY");
        }
    }

    @Test
    void testCreateContact() throws Exception {
        Contact contact = new Contact();
        contact.setName("John Doe");
        contact.setEmail("john.doe@example.com");
        contact.setDob(LocalDate.of(1990, 1, 1).toString());
        contact.setPhone("1234567890");

        Contact createdContact = contactDAO.createContact(contact);

        assertNotNull(createdContact);
        assertTrue(createdContact.getId() > 0);
        assertEquals("John Doe", createdContact.getName());
    }

    @Test
    void testUpdateContact() throws Exception {
        Contact contact = new Contact();
        contact.setName("John Doe");
        contact.setEmail("john.doe@example.com");
        contact.setDob(LocalDate.of(1990, 1, 1).toString());
        contact.setPhone("1234567890");

        Contact createdContact = contactDAO.createContact(contact);

        createdContact.setName("Jane Doe");
        createdContact.setPhone("0987654321");

        boolean updated = contactDAO.updateContact(createdContact);

        assertTrue(updated);
        Contact updatedContact = contactDAO.getContactById(createdContact.getId());
        assertEquals("Jane Doe", updatedContact.getName());
        assertEquals("0987654321", updatedContact.getPhone());
    }

    @Test
    void testDeleteContact() throws Exception {
        Contact contact = new Contact();
        contact.setName("John Doe");
        contact.setEmail("john.doe@example.com");
        contact.setDob(LocalDate.of(1990, 1, 1).toString());
        contact.setPhone("1234567890");

        Contact createdContact = contactDAO.createContact(contact);

        boolean deleted = contactDAO.deleteContact(createdContact.getId());

        assertTrue(deleted);
        Contact retrievedContact = contactDAO.getContactById(createdContact.getId());
        assertNull(retrievedContact);
    }

    @Test
    void testGetContactById() throws Exception {
        Contact contact = new Contact();
        contact.setName("John Doe");
        contact.setEmail("john.doe@example.com");
        contact.setDob(LocalDate.of(1990, 1, 1).toString());
        contact.setPhone("1234567890");

        Contact createdContact = contactDAO.createContact(contact);

        Contact retrievedContact = contactDAO.getContactById(createdContact.getId());

        assertNotNull(retrievedContact);
        assertEquals(createdContact.getId(), retrievedContact.getId());
        assertEquals("John Doe", retrievedContact.getName());
    }

    @Test
    void testSearchContacts() throws Exception {
        Contact contact1 = new Contact();
        contact1.setName("John Doe");
        contact1.setEmail("john.doe@example.com");
        contact1.setDob(LocalDate.of(1990, 1, 1).toString());
        contact1.setPhone("1234567890");
        contactDAO.createContact(contact1);

        Contact contact2 = new Contact();
        contact2.setName("Jane Smith");
        contact2.setEmail("jane.smith@example.com");
        contact2.setDob(LocalDate.of(1992, 2, 2).toString());
        contact2.setPhone("0987654321");
        contactDAO.createContact(contact2);

        List<Contact> contacts = contactDAO.searchContacts("Jane");

        assertFalse(contacts.isEmpty());
        assertEquals(1, contacts.size());
        assertEquals("Jane Smith", contacts.get(0).getName());
    }

    @Test
    void testGetAllContacts() throws Exception {
        Contact contact1 = new Contact();
        contact1.setName("John Doe");
        contact1.setEmail("john.doe@example.com");
        contact1.setDob(LocalDate.of(1990, 1, 1).toString());
        contact1.setPhone("1234567890");
        contactDAO.createContact(contact1);

        Contact contact2 = new Contact();
        contact2.setName("Jane Smith");
        contact2.setEmail("jane.smith@example.com");
        contact2.setDob(LocalDate.of(1992, 2, 2).toString());
        contact2.setPhone("0987654321");
        contactDAO.createContact(contact2);

        List<Contact> contacts = contactDAO.getAllContacts();

        assertFalse(contacts.isEmpty());
        assertEquals(2, contacts.size());
    }

    @AfterEach
    void cleanUp() throws SQLException {
        try (Connection conn = DatabaseReaderUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM contacts")) {
            stmt.executeUpdate();
        }
    }
}
