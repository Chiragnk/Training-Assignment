package com.monetize360.iplstats.service;

import com.monetize360.iplstats.dto.*;

import java.util.List;
import java.util.Map;

public interface TeamService {
    List<String> getTeamLabels();
    List<PlayerDTO> getPlayerByTeam(String label);
    List<RoleCountDTO> getCountByRole(String label);
    List<PlayerDTO> getPlayerByTeamAndRole(String label, String role);
    List<TeamDTO> getAllTeamDetails();
    List<TeamAmountDTO> getAmountSpentByTeams();
    List<TeamAmountByRoleDTO> getAmountSpentByTeamAndRole(String label, String role);
    Map<String, List<PlayerDTO>> getMaxPaidPlayersByRole();
    List<PlayerDTO> getPlayersBySort(String fieldName);
}
