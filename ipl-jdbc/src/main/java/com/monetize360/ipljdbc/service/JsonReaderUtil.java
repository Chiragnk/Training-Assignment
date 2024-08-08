package com.monetize360.ipljdbc.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monetize360.ipljdbc.domain.TeamDetails;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JsonReaderUtil {
     static List<TeamDetails> readTeams(String fileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = JsonReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IOException("File not found: " + fileName);
        }
        return objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }
}
