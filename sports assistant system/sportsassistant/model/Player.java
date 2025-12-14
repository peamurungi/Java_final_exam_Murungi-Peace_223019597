package com.sportsassistant.model;

public class Player {
    private int playerId;
    private String name;
    private String position;
    private int age;
    private int teamId;
    private String teamName;
    private int goals;
    private int assists;

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public int getGoals() { return goals; }
    public void setGoals(int goals) { this.goals = goals; }

    public int getAssists() { return assists; }
    public void setAssists(int assists) { this.assists = assists; }
}