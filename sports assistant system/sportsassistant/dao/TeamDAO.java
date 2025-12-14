package com.sportsassistant.dao;

import com.sportsassistant.config.DatabaseConfig;
import com.sportsassistant.model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public List<Team> getAllTeams() {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT team_id, team_name, city, founded_year, created_at FROM teams";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Team t = new Team();
                t.setTeamId(rs.getInt("team_id"));
                t.setTeamName(rs.getString("team_name"));
                t.setCity(rs.getString("city"));
                t.setFoundedYear(rs.getInt("founded_year"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                teams.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public void addTeam(Team t) {
        String sql = "INSERT INTO teams (team_name, city, founded_year) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, t.getTeamName());
            pst.setString(2, t.getCity());
            pst.setInt(3, t.getFoundedYear());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTeam(int teamId) {
        String sql = "DELETE FROM teams WHERE team_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, teamId);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
