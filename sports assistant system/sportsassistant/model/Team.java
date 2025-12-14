package com.sportsassistant.model;

import java.sql.Timestamp;

public class Team {
    private int teamId;
    private String teamName;
    private String city;
    private int foundedYear;
    private Timestamp createdAt;

    public Team() {}

    public Team(int teamId, String teamName, String city, int foundedYear, Timestamp createdAt) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.city = city;
        this.foundedYear = foundedYear;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public int getFoundedYear() { return foundedYear; }
    public void setFoundedYear(int foundedYear) { this.foundedYear = foundedYear; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
