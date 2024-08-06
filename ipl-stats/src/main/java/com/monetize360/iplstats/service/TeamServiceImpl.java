package com.monetize360.iplstats.service;

import com.monetize360.iplstats.domain.PlayerDetails;
import com.monetize360.iplstats.domain.TeamDetails;
import com.monetize360.iplstats.dto.*;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

public class TeamServiceImpl implements TeamService {
    private final JSONReaderUtil jsonReader = new JSONReaderUtil();
    private final ModelMapper modelMapper = new ModelMapper();
    private final String filename = "ipl2020.json";
    List<TeamDetails> teams = jsonReader.loadTeams(filename);

    public List<String> getTeamLabels() {
        return teams.stream()
                .map(TeamDetails::getLabel)
                .collect(Collectors.toList());
    }

    public List<PlayerDTO> getPlayerByTeam(String label) {
        return teams.stream()
                .filter(team -> team.getLabel().equalsIgnoreCase(label))
                .flatMap(team -> team.getPlayers().stream())
                .map(player -> modelMapper.map(player, PlayerDTO.class))
                .collect(Collectors.toList());
    }

    public List<RoleCountDTO> getCountByRole(String label) {
        return teams.stream()
                .filter(team -> team.getLabel().equalsIgnoreCase(label))
                .flatMap(team -> team.getPlayers().stream())
                .collect(Collectors.groupingBy(PlayerDetails::getRole, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new RoleCountDTO(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }


    public List<PlayerDTO> getPlayerByTeamAndRole(String label, String role) {
        return teams.stream()
                .filter(team -> team.getLabel().equalsIgnoreCase(label))
                .flatMap(team -> team.getPlayers().stream())
                .filter(player -> player.getRole().equalsIgnoreCase(role))
                .map(player -> modelMapper.map(player, PlayerDTO.class))
                .collect(Collectors.toList());
    }

    public List<TeamDTO> getAllTeamDetails() {
        return teams.stream()
                .map(team -> modelMapper.map(team, TeamDTO.class))
                .collect(Collectors.toList());
    }

    public List<TeamAmountDTO> getAmountSpentByTeams() {
        return teams.stream()
                .map(team -> {
                    int totalAmount = team.getPlayers().stream().mapToInt(PlayerDetails::getPrice).sum();
                    return new TeamAmountDTO(team.getLabel(), totalAmount);
                })
                .collect(Collectors.toList());
    }

    public List<TeamAmountByRoleDTO> getAmountSpentByTeamAndRole(String label, String role) {
        return teams.stream()
                .filter(team -> team.getLabel().equalsIgnoreCase(label))
                .map(team -> {
                    int totalAmount = (int) team.getPlayers().stream()
                            .filter(player -> player.getRole().equalsIgnoreCase(role))
                            .mapToDouble(PlayerDetails::getPrice)
                            .sum();
                    return new TeamAmountByRoleDTO(label, role, totalAmount);
                })
                .collect(Collectors.toList());
    }

    public Map<String, List<PlayerDTO>> getMaxPaidPlayersByRole() {
        Map<String, List<PlayerDTO>> playersByRole = teams.stream()
                .flatMap(team -> team.getPlayers().stream())
                .map(player -> modelMapper.map(player, PlayerDTO.class))
                .collect(Collectors.groupingBy(PlayerDTO::getRole));
        Map<String, List<PlayerDTO>> maxPaidPlayersByRole = new HashMap<>();
        playersByRole.forEach((role, players) -> {
            int maxPrice = players.stream()
                    .mapToInt(PlayerDTO::getPrice)
                    .max()
                    .orElse(0);
            List<PlayerDTO> topPaidPlayers = players.stream()
                    .filter(player -> player.getPrice() == maxPrice)
                    .collect(Collectors.toList());
            maxPaidPlayersByRole.put(role, topPaidPlayers);
        });
        return maxPaidPlayersByRole;
    }


    public List<PlayerDTO> getPlayersBySort(String fieldName) {
        List<PlayerDTO> players = teams.stream()
                .flatMap(team -> team.getPlayers().stream())
                .map(player -> modelMapper.map(player, PlayerDTO.class))
                .collect(Collectors.toList());
        Comparator<PlayerDTO> comparator;
        switch (fieldName) {
            case "price":
                comparator = Comparator.comparingInt(PlayerDTO::getPrice);
                break;
            case "name":
                comparator = Comparator.comparing(PlayerDTO::getName);
                break;
            case "role":
                comparator=Comparator.comparing(PlayerDTO::getRole);
                break;
            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
        players.sort(comparator);
        return players;
    }
}
