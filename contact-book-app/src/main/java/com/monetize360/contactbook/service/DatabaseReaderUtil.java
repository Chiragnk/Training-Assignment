package com.monetize360.contactbook.service;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

    public class DatabaseReaderUtil {
        private static final String PROPERTIES_FILE = "db.properties";
        private static Properties properties = new Properties();
        static {
            try (InputStream input = DatabaseReaderUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
                if (input == null) {
                    throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
                }
                properties.load(input);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load properties file", ex);
            }
        }
        private static final String URL = properties.getProperty("db.url");
        private static final String USER = properties.getProperty("db.user");
        private static final String PASSWORD = properties.getProperty("db.password");
        static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
