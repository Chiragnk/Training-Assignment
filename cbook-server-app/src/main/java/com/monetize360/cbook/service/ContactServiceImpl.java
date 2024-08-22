package com.monetize360.cbook.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monetize360.cbook.dao.ContactDAO;
import com.monetize360.cbook.domain.Contact;
import com.monetize360.cbook.dto.ContactDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ContactServiceImpl implements ContactService{
    @Autowired
    private ContactDAO contactDAO;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public ContactDTO insertContact(ContactDTO contactDTO) {
        Contact contact = objectMapper.convertValue(contactDTO, Contact.class);
        contact.setId(UUID.randomUUID());
        Contact savedContact = contactDAO.insertContact(contact);
        return objectMapper.convertValue(savedContact, ContactDTO.class);
    }

    @Override
    public ContactDTO updateContact(ContactDTO contactDTO) {
        Contact contact = objectMapper.convertValue(contactDTO, Contact.class);
        Contact updatedContact = contactDAO.updateContact(contact);
        return objectMapper.convertValue(updatedContact, ContactDTO.class);
    }

    @Override
    public ContactDTO getContactById(String id) {
        Contact contact = contactDAO.getContactById(id);
        return objectMapper.convertValue(contact, ContactDTO.class);
    }

    @Override
    public void deleteContact(UUID id) {
        contactDAO.deleteContact(id);
    }

    @Override
    public List<ContactDTO> getAllContacts(int page, int size, String sortBy, String sortDir) {
        List<Contact> contacts = contactDAO.getAllContacts(page, size, sortBy, sortDir);
        log.info("Contacts count is: {}", contacts.size());
        return objectMapper.convertValue(contacts, new TypeReference<List<ContactDTO>>() {});
    }


    @Override
    public List<ContactDTO> searchContacts(int page, int size, String sortBy, String sortDir, String search){
        List<Contact> contacts = contactDAO.searchContacts(page, size, sortBy, sortDir, search);
        return objectMapper.convertValue(contacts, new TypeReference<List<ContactDTO>>() {});
    }
}
