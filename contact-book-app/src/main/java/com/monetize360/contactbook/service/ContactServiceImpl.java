package com.monetize360.contactbook.service;

import com.monetize360.contactbook.domain.Contact;
import com.monetize360.contactbook.dto.ContactDTO;

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

    public java.sql.Date parseDate(String text) {
        if (text != null && !text.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(text);
                return new java.sql.Date(parsedDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    @Override
    public ContactDTO createContact(ContactDTO contactDTO) throws SQLException {
        Contact contact = modelMapper.map(contactDTO, Contact.class);
        String query = "INSERT INTO contacts (name, email, dob, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn=getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getEmail());
            stmt.setDate(3, parseDate(contact.getDob()));
            stmt.setString(4, contact.getPhone());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getInt(1));
                    contactDTO.setId(generatedKeys.getInt(1));
                }
            }
        }
        return contactDTO;
    }

    @Override
    public boolean updateContact(ContactDTO contactDTO) {
        String query = "UPDATE contacts SET name = ?, email = ?, dob = ?, phone = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, contactDTO.getName());
            pstmt.setString(2, contactDTO.getEmail());
            pstmt.setDate(3, parseDate(contactDTO.getDob()));
            pstmt.setString(4, contactDTO.getPhone());
            pstmt.setInt(5, contactDTO.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteContact(int contactId) {
        String query = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, contactId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ContactDTO getContactById(int contactId) {
        String query = "SELECT * FROM contacts WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, contactId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setName(rs.getString("name"));
                contact.setEmail(rs.getString("email"));
                contact.setDob(rs.getDate("dob").toString());
                contact.setPhone(rs.getString("phone"));
                return modelMapper.map(contact, ContactDTO.class);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ContactDTO> searchContacts(String keyword) {
        List<ContactDTO> result = new ArrayList<>();
        String query = "SELECT * FROM contacts WHERE name LIKE ? OR email LIKE ? OR phone LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchKeyword = "%" + keyword + "%";
            pstmt.setString(1, searchKeyword);
            pstmt.setString(2, searchKeyword);
            pstmt.setString(3, searchKeyword);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setName(rs.getString("name"));
                contact.setEmail(rs.getString("email"));
                contact.setDob(rs.getDate("dob").toString());
                contact.setPhone(rs.getString("phone"));

                result.add(modelMapper.map(contact, ContactDTO.class));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
    public boolean exportContactsToExcel(OutputStream outputStream) throws SQLException {
        List<ContactDTO> contacts = getAllContacts();
        System.out.println("Number of contacts retrieved: " + contacts.size());
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Contacts");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Name");
            header.createCell(1).setCellValue("Email");
            header.createCell(2).setCellValue("DOB");
            header.createCell(3).setCellValue("Phone");

            int rowNum = 1;
            for (ContactDTO contact : contacts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(contact.getName());
                row.createCell(1).setCellValue(contact.getEmail());
                row.createCell(2).setCellValue(contact.getDob());
                row.createCell(3).setCellValue(contact.getPhone());
            }
            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private List<ContactDTO> getAllContacts() throws SQLException {
        String query = "SELECT * FROM contacts";
        List<ContactDTO> contacts = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Contact contact = modelMapper.map(rs, Contact.class);
                contact.setName(rs.getString("name"));
                contact.setEmail(rs.getString("email"));
                contact.setDob(rs.getDate("dob").toString());
                contact.setPhone(rs.getString("phone"));

                contacts.add(modelMapper.map(contact, ContactDTO.class));
            }
        }
        return contacts;
    }
}

