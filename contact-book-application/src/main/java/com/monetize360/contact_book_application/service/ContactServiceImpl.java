package com.monetize360.contact_book_application.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.monetize360.contact_book_application.dao.ContactRepository;
import com.monetize360.contact_book_application.domain.Contact;
import com.monetize360.contact_book_application.dto.ContactDTO;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JavaMailSender mailSender;

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
        return objectMapper.convertValue(contactPage.getContent(), new TypeReference<List<ContactDTO>>() {});
    }

    @Override
    public List<ContactDTO> searchContacts(int page, int size, String sortBy, String sortDir, String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Contact> contactPage = contactRepository.findByFirstNameContainingOrLastNameContainingOrEmailContainingOrPhoneContaining(search, search, search, search, pageRequest);
        return objectMapper.convertValue(contactPage.getContent(), new TypeReference<List<ContactDTO>>() {});
    }
    @Override
    public BufferedImage generateQRCodeById(UUID id) throws IOException, WriterException {
        Optional<ContactDTO> contactDTO = getContactById(id);
        if (contactDTO.isPresent()) {
            String contactJson = objectMapper.writeValueAsString(contactDTO.get());
            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = barcodeWriter.encode(contactJson, BarcodeFormat.QR_CODE, 200, 200);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } else {
            return null;
        }
    }
    @Override
    public void importContacts(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Contact> contacts = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Contact contact = new Contact();
                contact.setFirstName(getCellValue((XSSFCell) row.getCell(0)));
                contact.setLastName(getCellValue((XSSFCell) row.getCell(1)));
                contact.setEmail(getCellValue((XSSFCell) row.getCell(2)));
                contact.setPhone(getCellValue((XSSFCell) row.getCell(3)));
                contacts.add(contact);
            }
            contactRepository.saveAll(contacts);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import contacts");
        }
    }

    @Override
    public byte[] exportContacts() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contacts");
            List<Contact> contacts = contactRepository.findAll();
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("First Name");
            headerRow.createCell(1).setCellValue("Last Name");
            headerRow.createCell(2).setCellValue("Email");
            headerRow.createCell(3).setCellValue("Phone");
            int rowIdx = 1;
            for (Contact contact : contacts) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(contact.getFirstName());
                row.createCell(1).setCellValue(contact.getLastName());
                row.createCell(2).setCellValue(contact.getEmail());
                row.createCell(3).setCellValue(contact.getPhone());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export contacts");
        }
    }
    public String getCellValue(XSSFCell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue()).toString();
        } else {
            return cell.toString();
        }
    }
        @Override
        public void importContactsFromCSV(MultipartFile file) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                String[] values;
                List<Contact> contacts = new ArrayList<>();
                while ((values = csvReader.readNext()) != null) {
                    if (values.length == 4) {
                        Contact contact = new Contact();
                        contact.setFirstName(values[0].trim());
                        contact.setLastName(values[1].trim());
                        contact.setEmail(values[2].trim());
                        contact.setPhone(values[3].trim());
                        contacts.add(contact);
                    }
                }
                contactRepository.saveAll(contacts);
            } catch (IOException | CsvValidationException e) {
                throw new RuntimeException("Failed to import contacts", e);
            }
        }

        @Override
        public byte[] exportContactsToCSV() {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                 CSVWriter csvWriter = new CSVWriter(writer)) {
                List<Contact> contacts = contactRepository.findAll();
                String[] header = {"First Name", "Last Name", "Email", "Phone"};
                csvWriter.writeNext(header);
                for (Contact contact : contacts) {
                    String[] record = {contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhone()};
                    csvWriter.writeNext(record);
                }
                csvWriter.flush();
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("Failed to export contacts to CSV", e);
            }
    }

    @Override
    public void shareContactByEmail(UUID id, String recipientEmail) throws IOException, WriterException, MessagingException {
        BufferedImage qrCodeImage = generateQRCodeById(id);
        if (qrCodeImage == null) {
            throw new RuntimeException("QR code generation failed. Contact not found.");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        byte[] qrCodeBytes = baos.toByteArray();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Contact QR Code");
        helper.setText("Here is the QR code for the contact you requested.");
        ByteArrayDataSource dataSource = new ByteArrayDataSource(qrCodeBytes, "image/png");
        helper.addAttachment("contact-qrcode.png", dataSource);
        mailSender.send(message);
    }
}

