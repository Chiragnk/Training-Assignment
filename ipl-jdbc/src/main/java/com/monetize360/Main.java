package com.monetize360;

import com.monetize360.ipljdbc.dto.PlayerDTO;
import com.monetize360.ipljdbc.dto.RoleCountDTO;
import com.monetize360.ipljdbc.dto.TeamAmountByRoleDTO;
import com.monetize360.ipljdbc.dto.TeamAmountDTO;
import com.monetize360.ipljdbc.dto.TeamDTO;
import com.monetize360.ipljdbc.service.DatabaseReaderUtil;
import com.monetize360.ipljdbc.service.TeamService;
import com.monetize360.ipljdbc.service.TeamServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseReaderUtil dbUtil = new DatabaseReaderUtil();
        dbUtil.initializeDatabase();

        TeamService teamService = new TeamServiceImpl();
        Scanner scanner = new Scanner(System.in);

        List<String> teamLabels = teamService.getTeamLabels();
        System.out.println("Team Labels:");
        teamLabels.forEach(System.out::println);

        System.out.print("Enter the team label to get player details: ");
        String teamLabel = scanner.nextLine();
        List<PlayerDTO> playersByTeam = teamService.getPlayerByTeam(teamLabel);
        System.out.println("Players in team " + teamLabel + ":");
        playersByTeam.forEach(player -> System.out.printf("Name: %s, Role: %s, Amount: %.2f%n",
                player.getName(),
                player.getRole(),
                player.getAmount()));

        System.out.print("Enter the team label to get count of players by role: ");
        String teamLabel1 = scanner.nextLine();
        List<RoleCountDTO> roleCounts = teamService.getCountByRole(teamLabel1);
        System.out.println("Player count by role for team " + teamLabel1 + ":");
        roleCounts.forEach(roleCount -> System.out.printf("Role: %s, Count: %d%n",
                roleCount.getRoleName(),
                roleCount.getCount()));

        System.out.print("Enter the team label to get players by team and role: ");
        String teamLabel2 = scanner.nextLine();
        System.out.print("Enter the role to filter players by: ");
        String role = scanner.nextLine();
        List<PlayerDTO> playersByTeamAndRole = teamService.getPlayerByTeamAndRole(teamLabel2, role);
        System.out.println("Players in team " + teamLabel2 + " with role " + role + ":");
        playersByTeamAndRole.forEach(player -> System.out.printf("Name: %s, Amount: %.2f%n",
                player.getName(),
                player.getAmount()));

        List<TeamDTO> allTeamDetails = teamService.getAllTeamDetails();
        System.out.println("All Team Details:");
        allTeamDetails.forEach(team -> System.out.printf("Home: %s, Label: %s, Name: %s, Coach: %s, City: %s%n",
                team.getHome(),
                team.getLabel(),
                team.getName(),
                team.getCoach(),
                team.getCity()));

        List<TeamAmountDTO> amountSpentByTeams = teamService.getAmountSpentByTeams();
        System.out.println("Amount Spent by Teams:");
        amountSpentByTeams.forEach(teamAmount -> System.out.printf("Team: %s, Total Amount: %.2f%n",
                teamAmount.getLabel(),
                teamAmount.getAmount()));

        System.out.print("Enter the team label to get amount spent by team and role: ");
        String teamLabel3 = scanner.nextLine();
        System.out.print("Enter the role to filter by: ");
        String role1 = scanner.nextLine();
        List<TeamAmountByRoleDTO> amountSpentByTeamAndRole = teamService.getAmountSpentByTeamAndRole(teamLabel3, role1);
        System.out.println("Amount Spent by Team " + teamLabel3 + " and Role " + role1 + ":");
        amountSpentByTeamAndRole.forEach(amount -> System.out.printf("Team: %s, Role: %s, Total Amount: %.2f%n",
                amount.getLabel(),
                amount.getRole(),
                amount.getAmount()));

        // Get max paid players by role
        Map<String, List<PlayerDTO>> maxPaidPlayersByRole = teamService.getMaxPaidPlayersByRole();
        System.out.println("Max Paid Players by Role:");
        maxPaidPlayersByRole.forEach((roleName, players) -> {
            System.out.println("Role: " + roleName);
            players.forEach(player -> System.out.printf("Name: %s, Amount: %.2f%n",
                    player.getName(),
                    player.getAmount()));
        });

        // Get players sorted by field
        System.out.print("Enter the field name to sort players by: ");
        String fieldName = scanner.nextLine();
        List<PlayerDTO> sortedPlayers = teamService.getPlayersBySort(fieldName);
        System.out.println("Players Sorted by " + fieldName + ":");
        sortedPlayers.forEach(player -> System.out.printf("Name: %s, Role: %s, Amount: %.2f%n",
                player.getName(),
                player.getRole(),
                player.getAmount()));

        scanner.close();
    }
}
