package com.sportsassistant.dao;

import com.sportsassistant.config.DatabaseConfig;
import com.sportsassistant.model.Statistic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticDAO {

    public void addStatistic(Statistic s) throws SQLException {
        String sql = "INSERT INTO statistics (player_id, matches_played, goals, assists) VALUES (?,?,?,?)";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, s.getPlayerId());
            stmt.setInt(2, s.getMatchesPlayed());
            stmt.setInt(3, s.getGoals());
            stmt.setInt(4, s.getAssists());
            stmt.executeUpdate();
        }
    }

    public void updateStatistic(Statistic s) throws SQLException {
        String sql = "UPDATE statistics SET player_id=?, matches_played=?, goals=?, assists=? WHERE statistic_id=?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, s.getPlayerId());
            stmt.setInt(2, s.getMatchesPlayed());
            stmt.setInt(3, s.getGoals());
            stmt.setInt(4, s.getAssists());
            stmt.setInt(5, s.getStatisticId());
            stmt.executeUpdate();
        }
    }

    public void deleteStatistic(int statisticId) throws SQLException {
        String sql = "DELETE FROM statistics WHERE statistic_id=?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, statisticId);
            stmt.executeUpdate();
        }
    }

    public List<Statistic> getAllStatistics() throws SQLException {
        List<Statistic> list = new ArrayList<>();
        String sql = "SELECT * FROM statistics";
        try(Connection conn = DatabaseConfig.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Statistic s = new Statistic(
                        rs.getInt("statistic_id"),
                        rs.getInt("player_id"),
                        rs.getInt("matches_played"),
                        rs.getInt("goals"),
                        rs.getInt("assists")
                );
                list.add(s);
            }
        }
        return list;
    }

    // ---- Junction table methods ----
    public void linkScoreToStatistic(int scoreId, int statisticId) throws SQLException {
        String sql = "INSERT INTO score_statistic (score_id, statistic_id) VALUES (?, ?)";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, scoreId);
            stmt.setInt(2, statisticId);
            stmt.executeUpdate();
        }
    }

    public void unlinkScoreFromStatistic(int scoreId, int statisticId) throws SQLException {
        String sql = "DELETE FROM score_statistic WHERE score_id=? AND statistic_id=?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, scoreId);
            stmt.setInt(2, statisticId);
            stmt.executeUpdate();
        }
    }

    public List<Integer> getScoresForStatistic(int statisticId) throws SQLException {
        List<Integer> scores = new ArrayList<>();
        String sql = "SELECT score_id FROM score_statistic WHERE statistic_id=?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, statisticId);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    scores.add(rs.getInt("score_id"));
                }
            }
        }
        return scores;
    }
}
