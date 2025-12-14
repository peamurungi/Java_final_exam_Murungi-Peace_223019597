package com.sportsassistant.dao;

import com.sportsassistant.config.DatabaseConfig;
import com.sportsassistant.model.Match;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    public List<Match> getAllMatches() {
        List<Match> list = new ArrayList<>();
        String sql = "SELECT match_id, team1_id, team2_id, match_date, location, created_at FROM matches";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Match m = new Match();
                m.setMatchId(rs.getInt("match_id"));
                m.setTeam1Id(rs.getInt("team1_id"));
                m.setTeam2Id(rs.getInt("team2_id"));
                m.setMatchDate(rs.getDate("match_date")); // java.sql.Date
                m.setLocation(rs.getString("location"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addMatch(Match m) {
        String sql = "INSERT INTO matches (team1_id, team2_id, match_date, location) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, m.getTeam1Id());
            pst.setInt(2, m.getTeam2Id());
            pst.setDate(3, m.getMatchDate()); // java.sql.Date
            pst.setString(4, m.getLocation());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMatch(int matchId) {
        String sql = "DELETE FROM matches WHERE match_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, matchId);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
