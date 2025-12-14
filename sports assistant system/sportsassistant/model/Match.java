package com.sportsassistant.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Match {
    private int matchId;
    private int team1Id;
    private int team2Id;
    private Date matchDate; // use java.sql.Date
    private String location;
    private Timestamp createdAt;

    // Getters and setters
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public int getTeam1Id() { return team1Id; }
    public void setTeam1Id(int team1Id) { this.team1Id = team1Id; }

    public int getTeam2Id() { return team2Id; }
    public void setTeam2Id(int team2Id) { this.team2Id = team2Id; }

    public Date getMatchDate() { return matchDate; }
    public void setMatchDate(Date matchDate) { this.matchDate = matchDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
