package com.monetize360.iplstats.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeamDetails {
    private String home;
    private String label;
    private String name;
    private String coach;
    private String city;
    private List<PlayerDetails>players;

}
