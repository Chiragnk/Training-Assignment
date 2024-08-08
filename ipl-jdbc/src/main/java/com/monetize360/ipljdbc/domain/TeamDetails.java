package com.monetize360.ipljdbc.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class TeamDetails {
    private String home;
    private String label;
    private String name;
    private String coach;
    private String city;
    private List<PlayerDetails> players;
}
