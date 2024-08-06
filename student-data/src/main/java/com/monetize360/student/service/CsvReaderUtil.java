package com.monetize360.student.service;

import com.monetize360.student.domain.StudentDetails;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderUtil {

    public static List<String[]> readCsv(String fileName) throws IOException, CsvException {
        List<String[]> records = new ArrayList<>();
        try (InputStream inputStream = CsvReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReader(inputStreamReader)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                records.add(nextRecord);
            }
        }
        return records;
    }
    
}
