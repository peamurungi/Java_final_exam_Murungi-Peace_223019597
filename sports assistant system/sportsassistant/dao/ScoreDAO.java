package com.sportsassistant.dao;

import com.sportsassistant.config.DatabaseConfig;
import com.sportsassistant.model.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public List<Score> getAllScores() {
        List<Score> list = new ArrayList<>();
        String sql = "SELECT score_id, match_id, team_id, points, created_at FROM scores";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Score s = new Score();
                s.setScoreId(rs.getInt("score_id"));
                s.setMatchId(rs.getInt("match_id"));
                s.setTeamId(rs.getInt("team_id"));
                s.setPoints(rs.getInt("points"));
                s.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addScore(Score s) {
        String sql = "INSERT INTO scores (match_id, team_id, points) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, s.getMatchId());
            pst.setInt(2, s.getTeamId());
            pst.setInt(3, s.getPoints());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteScore(int scoreId) {
        String sql = "DELETE FROM scores WHERE score_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, scoreId);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
