package com.sportsassistant.dao;

import com.sportsassistant.config.DatabaseConfig;
import com.sportsassistant.model.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    public List<Player> getAllPlayers() {
        List<Player> list = new ArrayList<>();
        String sql = "SELECT player_id, name, position, age, team_id FROM players"; 

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Player p = new Player();
                p.setPlayerId(rs.getInt("player_id"));
                p.setName(rs.getString("name"));
                p.setPosition(rs.getString("position"));
                p.setAge(rs.getInt("age"));
                p.setTeamId(rs.getInt("team_id"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addPlayer(Player p) {
        String sql = "INSERT INTO players (name, position, age, team_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, p.getName());
            pst.setString(2, p.getPosition());
            pst.setInt(3, p.getAge());
            pst.setInt(4, p.getTeamId());
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayer(int playerId) {
        String sql = "DELETE FROM players WHERE player_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, playerId);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
