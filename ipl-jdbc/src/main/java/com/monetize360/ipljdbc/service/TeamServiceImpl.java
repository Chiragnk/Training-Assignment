package com.monetize360.ipljdbc.service;

import com.monetize360.ipljdbc.dto.*;
import java.sql.*;
import java.util.*;

import static com.monetize360.ipljdbc.service.DatabaseReaderUtil.getConnection;

public class TeamServiceImpl implements TeamService {

    @Override
    public List<String> getTeamLabels() {
        String query = "SELECT label FROM teams";
        List<String> labels = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                labels.add(rs.getString("label"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return labels;
    }

    @Override
    public List<PlayerDTO> getPlayerByTeam(String label) {
        String query = "SELECT * FROM players p JOIN teams t ON p.team_name = t.name WHERE t.label = ?";
        List<PlayerDTO> playerDTOs = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, label);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setName(rs.getString("name"));
                    playerDTO.setRole(rs.getString("role"));
                    playerDTO.setAmount(rs.getDouble("amount"));
                    playerDTOs.add(playerDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerDTOs;
    }

    @Override
    public List<RoleCountDTO> getCountByRole(String label) {
        String query = "SELECT role, COUNT(*) AS count FROM players p JOIN teams t ON p.team_name = t.name WHERE t.label = ? GROUP BY role";
        List<RoleCountDTO> roleCountDTOs = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, label);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RoleCountDTO roleCountDTO = new RoleCountDTO();
                    roleCountDTO.setRoleName(rs.getString("role"));
                    roleCountDTO.setCount(rs.getInt("count"));
                    roleCountDTOs.add(roleCountDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleCountDTOs;
    }

    @Override
    public List<PlayerDTO> getPlayerByTeamAndRole(String label, String role) {
        String query = "SELECT * FROM players p JOIN teams t ON p.team_name = t.name WHERE t.label = ? AND p.role = ?";
        List<PlayerDTO> playerDTOs = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, label);
            stmt.setString(2, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PlayerDTO playerDTO = new PlayerDTO();
                    playerDTO.setName(rs.getString("name"));
                    playerDTO.setAmount(rs.getDouble("amount"));
                    playerDTOs.add(playerDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerDTOs;
    }

    @Override
    public List<TeamDTO> getAllTeamDetails() {
        String query = "SELECT * FROM teams";
        List<TeamDTO> teamDTOs = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TeamDTO teamDTO = new TeamDTO();
                teamDTO.setHome(rs.getString("home"));
                teamDTO.setLabel(rs.getString("label"));
                teamDTO.setName(rs.getString("name"));
                teamDTO.setCoach(rs.getString("coach"));
                teamDTO.setCity(rs.getString("city"));
                teamDTOs.add(teamDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamDTOs;
    }

    @Override
    public List<TeamAmountDTO> getAmountSpentByTeams() {
        String query = "SELECT t.label, SUM(p.amount) AS total_amount FROM teams t JOIN players p ON t.name = p.team_name GROUP BY t.label";
        List<TeamAmountDTO> teamAmountDTOs = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TeamAmountDTO teamAmountDTO = new TeamAmountDTO();
                teamAmountDTO.setLabel(rs.getString("label"));
                teamAmountDTO.setAmount(rs.getDouble("total_amount"));
                teamAmountDTOs.add(teamAmountDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamAmountDTOs;
    }

    @Override
    public List<TeamAmountByRoleDTO> getAmountSpentByTeamAndRole(String label, String role) {
        String query = "SELECT t.label, p.role, SUM(p.amount) AS total_amount FROM teams t JOIN players p ON t.name = p.team_name WHERE t.label = ? AND p.role = ? GROUP BY t.label, p.role";
        List<TeamAmountByRoleDTO> teamAmountByRoleDTOs = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, label);
            stmt.setString(2, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TeamAmountByRoleDTO teamAmountByRoleDTO = new TeamAmountByRoleDTO();
                    teamAmountByRoleDTO.setLabel(rs.getString("label"));
                    teamAmountByRoleDTO.setRole(rs.getString("role"));
                    teamAmountByRoleDTO.setAmount(rs.getDouble("total_amount"));
                    teamAmountByRoleDTOs.add(teamAmountByRoleDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamAmountByRoleDTOs;
    }

    @Override
    public List<PlayerDTO> getPlayersBySort(String fieldName) {
        String query = "SELECT * FROM players ORDER BY " + fieldName;
        List<PlayerDTO> playerDTOs = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PlayerDTO playerDTO = new PlayerDTO();
                playerDTO.setName(rs.getString("name"));
                playerDTO.setRole(rs.getString("role"));
                playerDTO.setAmount(rs.getDouble("amount"));
                playerDTOs.add(playerDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerDTOs;
    }

    @Override
    public Map<String, List<PlayerDTO>> getMaxPaidPlayersByRole() {
        String query = "SELECT role AS player_role, name, amount FROM players WHERE amount = ( SELECT MAX(amount) FROM players p WHERE p.role = players.role ) GROUP BY role, name, amount";
        Map<String, List<PlayerDTO>> maxPaidPlayersMap = new HashMap<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("player_role");
                PlayerDTO playerDTO = new PlayerDTO();
                playerDTO.setName(rs.getString("name"));
                playerDTO.setRole(role);
                playerDTO.setAmount(rs.getDouble("amount"));

                maxPaidPlayersMap
                        .computeIfAbsent(role, k -> new ArrayList<>())
                        .add(playerDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxPaidPlayersMap;
    }
}
