package com.sportsassistant.dao;

import com.sportsassistant.config.DatabaseConfig;
import com.sportsassistant.model.Coach;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachDAO {
    public void addCoach(Coach coach) throws SQLException {
        String sql = "INSERT INTO coaches (name, experience, team_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, coach.getName());
            stmt.setInt(2, coach.getExperience());
            stmt.setInt(3, coach.getTeamId());
            stmt.executeUpdate();
        }
    }

    public void updateCoach(Coach coach) throws SQLException {
        String sql = "UPDATE coaches SET name=?, experience=?, team_id=? WHERE coach_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, coach.getName());
            stmt.setInt(2, coach.getExperience());
            stmt.setInt(3, coach.getTeamId());
            stmt.setInt(4, coach.getCoachId());
            stmt.executeUpdate();
        }
    }

    public void deleteCoach(int coachId) throws SQLException {
        String sql = "DELETE FROM coaches WHERE coach_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.executeUpdate();
        }
    }

    public List<Coach> getAllCoaches() throws SQLException {
        List<Coach> list = new ArrayList<>();
        String sql = "SELECT * FROM coaches";
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Coach c = new Coach();
                c.setCoachId(rs.getInt("coach_id"));
                c.setName(rs.getString("name"));
                c.setExperience(rs.getInt("experience"));
                c.setTeamId(rs.getInt("team_id"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(c);
            }
        }
        return list;
    }
}
