package com.monetize360.contactbook.service;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

    public class DatabaseReaderUtil {
       /* private static final String PROPERTIES_FILE = "db.properties";
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
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }*/
        private static final String PROPERTIES_FILE = "db.properties";
        private static Properties properties = new Properties();
        private static String schema;

        static {
            loadProperties(PROPERTIES_FILE);
            schema = properties.getProperty("db.schema");
        }

        private static void loadProperties(String propertiesFileName) {
            try (InputStream input = DatabaseReaderUtil.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
                if (input == null) {
                    throw new RuntimeException("Unable to find " + propertiesFileName);
                }
                properties.load(input);
            } catch (IOException ex) {
                throw new RuntimeException("Failed to load properties file: " + propertiesFileName, ex);
            }
        }

        public static void useTestSchema() {
            schema = properties.getProperty("db.test.schema");
        }

        public static Connection getConnection() throws SQLException {
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            Connection connection = DriverManager.getConnection(url, user, password);
            if (schema != null && !schema.isEmpty()) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("SET search_path TO " + schema);
                }
            }
            return connection;
        }
    }
