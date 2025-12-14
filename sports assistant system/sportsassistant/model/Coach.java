package com.sportsassistant.model;

import java.sql.Timestamp;

public class Coach {
    private int coachId;
    private String name;
    private int experience;
    private int teamId;
    private Timestamp createdAt;

    public Coach() {}

    public Coach(int coachId, String name, int experience, int teamId) {
        this.coachId = coachId;
        this.name = name;
        this.experience = experience;
        this.teamId = teamId;
    }

    public int getCoachId() { return coachId; }
    public void setCoachId(int coachId) { this.coachId = coachId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
