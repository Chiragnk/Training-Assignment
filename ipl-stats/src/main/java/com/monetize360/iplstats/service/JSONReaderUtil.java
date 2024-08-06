package com.monetize360.iplstats.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monetize360.iplstats.domain.PlayerDetails;
import com.monetize360.iplstats.domain.TeamDetails;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONReaderUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();
    public List<TeamDetails> loadTeams(String filename) {
        List<TeamDetails> teams = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + filename);
            }
            TeamDetails[] teamArray = objectMapper.readValue(inputStream, TeamDetails[].class);
            for (TeamDetails team : teamArray) {
                teams.add(team);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public List<PlayerDetails> loadPlayers(String filename) {
        List<PlayerDetails> players = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + filename);
            }
            JsonNode rootArray = objectMapper.readTree(inputStream);
            for (JsonNode teamNode : rootArray) {
                JsonNode playersNode = teamNode.path("players");
                for (JsonNode playerNode : playersNode) {
                    PlayerDetails player = objectMapper.readValue(playerNode.toString(), PlayerDetails.class);
                    players.add(player);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return players;
    }
}
