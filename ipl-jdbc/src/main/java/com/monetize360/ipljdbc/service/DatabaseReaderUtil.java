package com.monetize360.ipljdbc.service;

import com.monetize360.ipljdbc.domain.PlayerDetails;
import com.monetize360.ipljdbc.domain.TeamDetails;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
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

    private static String URL = properties.getProperty("db.url");
    private static String USER = properties.getProperty("db.user");
    private static String PASSWORD = properties.getProperty("db.password");

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void initializeDatabase() {
        List<TeamDetails> teams;
        try {
            teams = JsonReaderUtil.readTeams("ipl2020.json");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        try (Connection connection = getConnection()) {
            String createTeamsTable = "CREATE TABLE IF NOT EXISTS teams (" +
                    "id SERIAL PRIMARY KEY, " +
                    "home VARCHAR(255), " +
                    "label VARCHAR(255), " +
                    "name VARCHAR(255) UNIQUE NOT NULL, " +
                    "coach VARCHAR(255), " +
                    "city VARCHAR(255)" +
                    ")";

            String createPlayersTable = "CREATE TABLE IF NOT EXISTS players (" +
                    "id SERIAL PRIMARY KEY, " +
                    "team_name VARCHAR(255) REFERENCES teams(name), " +
                    "role VARCHAR(255), " +
                    "name VARCHAR(255), " +
                    "amount DOUBLE" +
                    ")";

            try (PreparedStatement createTeamsStmt = connection.prepareStatement(createTeamsTable);
                 PreparedStatement createPlayersStmt = connection.prepareStatement(createPlayersTable)) {
                createTeamsStmt.execute();
                createPlayersStmt.execute();
            }

            if (isTableEmpty(connection, "teams")) {
                String teamSql = "INSERT INTO teams (home, label, name, coach, city) VALUES (?, ?, ?, ?, ?) ";
                try (PreparedStatement teamStmt = connection.prepareStatement(teamSql)) {
                    for (TeamDetails team : teams) {
                        teamStmt.setString(1, team.getHome());
                        teamStmt.setString(2, team.getLabel());
                        teamStmt.setString(3, team.getName());
                        teamStmt.setString(4, team.getCoach());
                        teamStmt.setString(5, team.getCity());
                        teamStmt.addBatch();
                    }
                    teamStmt.executeBatch();
                }
            }

            if (isTableEmpty(connection, "players")) {
                String playerSql = "INSERT INTO players (team_name, role, name, amount) VALUES (?, ?, ?, ?) ";
                try (PreparedStatement playerStmt = connection.prepareStatement(playerSql)) {
                    for (TeamDetails team : teams) {
                        for (PlayerDetails player : team.getPlayers()) {
                            playerStmt.setString(1, team.getName());
                            playerStmt.setString(2, player.getRole());
                            playerStmt.setString(3, player.getName());
                            playerStmt.setDouble(4, player.getPrice());
                            playerStmt.addBatch();
                        }
                    }
                    playerStmt.executeBatch();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isTableEmpty(Connection connection, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }
}
