package com.monetize360.contactbook.service;

import com.monetize360.contactbook.dao.ContactDAO;
import com.monetize360.contactbook.dao.ContactDAOImpl;
import com.monetize360.contactbook.domain.Contact;
import com.monetize360.contactbook.dto.ContactDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import static com.monetize360.contactbook.service.DatabaseReaderUtil.getConnection;

    public class ContactServiceImpl implements ContactService {
        private ModelMapper modelMapper = new ModelMapper();
        private ContactDAO contactDAO = new ContactDAOImpl();

        @Override
        public ContactDTO createContact(ContactDTO contactDTO) throws SQLException {
            Contact contact = modelMapper.map(contactDTO, Contact.class);
            contact = contactDAO.createContact(contact);
            return modelMapper.map(contact, ContactDTO.class);
        }

        @Override
        public boolean updateContact(ContactDTO contactDTO) throws SQLException {
            Contact contact = modelMapper.map(contactDTO, Contact.class);
            return contactDAO.updateContact(contact);
        }

        @Override
        public boolean deleteContact(int contactId) throws SQLException {
            return contactDAO.deleteContact(contactId);
        }

        @Override
        public ContactDTO getContactById(int contactId) throws SQLException {
            Contact contact = contactDAO.getContactById(contactId);
            return contact != null ? modelMapper.map(contact, ContactDTO.class) : null;
        }

        @Override
        public List<ContactDTO> searchContacts(String keyword) throws SQLException {
            List<Contact> contacts = contactDAO.searchContacts(keyword);
            List<ContactDTO> contactDTOs = new ArrayList<>();
            for (Contact contact : contacts) {
                contactDTOs.add(modelMapper.map(contact, ContactDTO.class));
            }
            return contactDTOs;
        }

    @Override
    public boolean importContactsFromExcel(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                ContactDTO contactDTO = new ContactDTO();

                if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.STRING) {
                    contactDTO.setName(row.getCell(0).getStringCellValue());
                } else if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.NUMERIC) {
                    contactDTO.setName(String.valueOf(row.getCell(0).getNumericCellValue()));
                }

                if (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.STRING) {
                    contactDTO.setEmail(row.getCell(1).getStringCellValue());
                } else if (row.getCell(1) != null && row.getCell(1).getCellType() == CellType.NUMERIC) {
                    contactDTO.setEmail(String.valueOf(row.getCell(1).getNumericCellValue()));
                }

                if (row.getCell(2) != null && row.getCell(2).getCellType() == CellType.NUMERIC) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    contactDTO.setDob(dateFormat.format(row.getCell(2).getDateCellValue()));
                }

                if (row.getCell(3) != null) {
                    if (row.getCell(3).getCellType() == CellType.STRING) {
                        contactDTO.setPhone(row.getCell(3).getStringCellValue().trim());
                    } else if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                        contactDTO.setPhone(String.valueOf(row.getCell(3).getNumericCellValue()));
                    }
                }
                createContact(contactDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    @Override
    public boolean exportContactsToExcel(OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contacts");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Email", "Date of Birth", "Phone"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            List<ContactDTO> contacts = getAllContacts();
            int rowNum = 1;
            for (ContactDTO contact : contacts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(contact.getName());
                row.createCell(1).setCellValue(contact.getEmail());
                row.createCell(2).setCellValue(contact.getDob());
                row.createCell(3).setCellValue(contact.getPhone());
            }

            workbook.write(outputStream);
            return true;
        } catch (IOException | SQLException e) {
            System.err.println("Failed to export contacts to Excel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


        private List<ContactDTO> getAllContacts() throws SQLException {
            String query = "SELECT * FROM contacts";
            List<ContactDTO> contacts = new ArrayList<>();
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Contact contact = new Contact();
                    contact.setName(rs.getString("name"));
                    contact.setEmail(rs.getString("email"));
                    contact.setDob(rs.getString("dob"));
                    contact.setPhone(rs.getString("phone"));

                    ContactDTO contactDTO = modelMapper.map(contact, ContactDTO.class);
                    contacts.add(contactDTO);

                    System.out.println("Retrieved ContactDTO: " + contactDTO);
                }
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
                e.printStackTrace();
            }
            return contacts;
        }

    }

