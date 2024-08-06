package com.monetize360;
import com.monetize360.iplstats.dto.*;
import com.monetize360.iplstats.service.TeamServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TeamServiceImpl teamService = new TeamServiceImpl();
        List<String> teamLabels = teamService.getTeamLabels();
        for (String label : teamLabels) {
            System.out.println("Team Label: " + label);
        }
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the team label to get player details: ");
        String teamLabel = scanner.nextLine();
        List<PlayerDTO> players = teamService.getPlayerByTeam(teamLabel);
        System.out.println("Players for team with label '" + teamLabel + "':");
        for (PlayerDTO player : players) {
            System.out.println("Name: " + player.getName());
        }
        System.out.print("Enter the team label to get role counts: ");
        String teamLabel1 = scanner.nextLine();
        List<RoleCountDTO> roleCounts = teamService.getCountByRole(teamLabel1);

        System.out.println("Role counts for team with label '" + teamLabel1 + "':");
            for (RoleCountDTO roleCount : roleCounts) {
                System.out.println("Role: " + roleCount.getRoleName() + ", Count: " + roleCount.getCount());
        }

        System.out.print("Enter the team label to get player details: ");
        String teamLabel2 = scanner.nextLine();
        System.out.print("Enter the role to filter players by: ");
        String role = scanner.nextLine();
        List<PlayerDTO> players1 = teamService.getPlayerByTeamAndRole(teamLabel2, role);
        System.out.println("\nPlayers for team with label '" + teamLabel2 + "' and role '" + role + "':");
            for (PlayerDTO player : players1) {
                System.out.println("Name: " + player.getName() + ", Price: " + player.getPrice() + ", Role: " + player.getRole());
        }

        List<TeamDTO> teams = teamService.getAllTeamDetails();
        System.out.println("All team details:");
            for (TeamDTO team : teams) {
                System.out.println("City: " + team.getCity() + ", Home: " + team.getHome() + ", Coach: " + team.getCoach() + ", Team: " + team.getName() + ", Label: " + team.getLabel());
        }

        List<TeamAmountDTO> teamAmounts = teamService.getAmountSpentByTeams();
        System.out.println("Amount spent by each team:");
            for (TeamAmountDTO teamAmount : teamAmounts) {
                System.out.println("Team Label: " + teamAmount.getLabel() + ", Amount Spent: " + teamAmount.getAmount());
        }

        System.out.print("Enter the team label: ");
        String teamLabel3 = scanner.nextLine();
        System.out.print("Enter the role to filter players by: ");
        String role1 = scanner.nextLine();
        List<TeamAmountByRoleDTO> amountsByRole = teamService.getAmountSpentByTeamAndRole(teamLabel3, role1);
        System.out.println("\nAmount spent by team '" + teamLabel3 + "' on players with role '" + role1 + "':");
            for (TeamAmountByRoleDTO amountByRole : amountsByRole) {
                System.out.println("Team Label: " + amountByRole.getLabel() + ", Role: " + amountByRole.getRole() + ", Amount Spent: " + amountByRole.getAmount());
        }

        Map<String, List<PlayerDTO>> maxPaidPlayersByRole = teamService.getMaxPaidPlayersByRole();
        System.out.println("Max-paid players by role:");
        for (Map.Entry<String, List<PlayerDTO>> entry : maxPaidPlayersByRole.entrySet()) {
            String role2 = entry.getKey();
            List<PlayerDTO> players2 = entry.getValue();
            System.out.println("Role: " + role2);
            for (PlayerDTO player : players2) {
                System.out.println("  Name: " + player.getName() + ", Price: " + player.getPrice());
            }
        }

        System.out.print("Enter the field name to sort by ");
        String fieldName = scanner.nextLine();
        List<PlayerDTO> sortedPlayers = teamService.getPlayersBySort(fieldName);
        System.out.println("Players sorted by '" + fieldName + "':");
        for (PlayerDTO player : sortedPlayers) {
            System.out.println("Name: " + player.getName() + ", Price: " + player.getPrice() + ", Role: " + player.getRole());
        }
    }
}