package com.sportsassistant.dao;

import com.sportsassistant.config.DatabaseConfig;
import com.sportsassistant.model.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchPlayerDAO {

    // Add a player to a match
    public boolean addPlayerToMatch(int matchId, int playerId) {
        String sql = "INSERT INTO match_players (match_id, player_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, matchId);
            pst.setInt(2, playerId);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Remove a player from a match
    public boolean removePlayerFromMatch(int matchId, int playerId) {
        String sql = "DELETE FROM match_players WHERE match_id = ? AND player_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, matchId);
            pst.setInt(2, playerId);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Return full player objects for this match
    public List<Player> getPlayersInMatch(int matchId) {
        List<Player> players = new ArrayList<>();
        String sql = """
            SELECT p.player_id, p.name, p.position
            FROM match_players mp
            JOIN players p ON mp.player_id = p.player_id
            WHERE mp.match_id = ?
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, matchId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Player player = new Player();
                player.setPlayerId(rs.getInt("player_id"));
                player.setName(rs.getString("name"));
                player.setPosition(rs.getString("position"));
                players.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
}
