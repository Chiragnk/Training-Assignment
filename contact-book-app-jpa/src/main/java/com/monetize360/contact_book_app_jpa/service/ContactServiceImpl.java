package com.monetize360.contact_book_app_jpa.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monetize360.contact_book_app_jpa.dao.ContactRepository;
import com.monetize360.contact_book_app_jpa.domain.Contact;
import com.monetize360.contact_book_app_jpa.dto.ContactDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ContactDTO insertContact(ContactDTO contactDTO) {
        Contact contact = objectMapper.convertValue(contactDTO, Contact.class);
        contact.setId(UUID.randomUUID());
        contact = contactRepository.save(contact);
        return objectMapper.convertValue(contact, ContactDTO.class);
    }

    @Override
    public ContactDTO updateContact(ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(contactDTO.getId()).orElseThrow(() -> new RuntimeException("Contact not found"));
        contact.setFirstName(contactDTO.getFirstName());
        contact.setLastName(contactDTO.getLastName());
        contact.setEmail(contactDTO.getEmail());
        contact.setPhone(contactDTO.getPhone());
        contact.setDeleted(contactDTO.isDeleted());
        Contact updatedContact = contactRepository.save(contact);
        return objectMapper.convertValue(updatedContact, ContactDTO.class);
    }

    @Override
    public Optional<ContactDTO> getContactById(UUID id) {
        return contactRepository.findById(id).map(contact -> objectMapper.convertValue(contact, ContactDTO.class));
    }

    @Override
    public void deleteContact(UUID id) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        if (optionalContact.isPresent()) {
            Contact contact = optionalContact.get();
            contact.setDeleted(true);
            contactRepository.save(contact);
        } else {
            throw new RuntimeException("Contact not found");
        }
    }

    @Override
    public List<ContactDTO> getAllContacts(int page, int size, String sortBy, String sortDir) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Contact> contactPage = contactRepository.findAll(pageRequest);
        List<ContactDTO> contactDTOs = new ArrayList<>();
        for (Contact contact : contactPage.getContent()) {
            ContactDTO contactDTO = objectMapper.convertValue(contact, ContactDTO.class);
            contactDTOs.add(contactDTO);
        }
        return contactDTOs;
    }

    @Override
    public List<ContactDTO> searchContacts(int page, int size, String sortBy, String sortDir, String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Contact> contactPage = contactRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingOrPhoneContaining(search, search, search, search, pageRequest);
        List<ContactDTO> contactDTOs = new ArrayList<>();
        for (Contact contact : contactPage.getContent()) {
            ContactDTO contactDTO = objectMapper.convertValue(contact, ContactDTO.class);
            contactDTOs.add(contactDTO);
        }
        return contactDTOs;
    }
}
