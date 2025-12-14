package com.sportsassistant.model;

import java.sql.Timestamp;

public class Statistic {
    private int statisticId;
    private int playerId;
    private int matchesPlayed;
    private int goals;
    private int assists;
    private Timestamp createdAt;

    public Statistic() {}

    public Statistic(int statisticId, int playerId, int matchesPlayed, int goals, int assists) {
        this.statisticId = statisticId;
        this.playerId = playerId;
        this.matchesPlayed = matchesPlayed;
        this.goals = goals;
        this.assists = assists;
    }

    public int getStatisticId() { return statisticId; }
    public void setStatisticId(int statisticId) { this.statisticId = statisticId; }
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public int getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }
    public int getGoals() { return goals; }
    public void setGoals(int goals) { this.goals = goals; }
    public int getAssists() { return assists; }
    public void setAssists(int assists) { this.assists = assists; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
