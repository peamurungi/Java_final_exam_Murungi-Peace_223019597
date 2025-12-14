package com.sportsassistant.panels;

import com.sportsassistant.dao.PlayerDAO;
import com.sportsassistant.dao.TeamDAO;
import com.sportsassistant.model.Player;
import com.sportsassistant.model.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addButton, deleteButton, refreshButton;

    private PlayerDAO playerDAO = new PlayerDAO();
    private TeamDAO teamDAO = new TeamDAO();
    private Map<Integer, String> teamMap = new HashMap<>();

    public PlayerPanel() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add titled border
        setBorder(BorderFactory.createTitledBorder(" Player Management "));

        // Table columns
        String[] columns = {"Player ID", "Name", "Position", "Age", "Team"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // Table styling
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(200, 230, 255));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        addButton = new JButton("Add Player");
        deleteButton = new JButton("Delete Player");
        refreshButton = new JButton("Refresh");

        // Button styling
        Font btnFont = new Font("Segoe UI", Font.BOLD, 13);
        addButton.setFont(btnFont);
        deleteButton.setFont(btnFont);
        refreshButton.setFont(btnFont);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadTeams();
        loadPlayers();

        // Actions
        addButton.addActionListener(e -> addPlayer());
        deleteButton.addActionListener(e -> deletePlayer());
        refreshButton.addActionListener(e -> loadPlayers());
    }

    private void loadTeams() {
        teamMap.clear();
        List<Team> teams = teamDAO.getAllTeams();
        for (Team t : teams) {
            teamMap.put(t.getTeamId(), t.getTeamName());
        }
    }

    private void loadPlayers() {
        model.setRowCount(0);
        List<Player> players = playerDAO.getAllPlayers();
        for (Player p : players) {
            Object[] row = {
                p.getPlayerId(),
                p.getName(),
                p.getPosition(),
                p.getAge(),
                teamMap.getOrDefault(p.getTeamId(), "Unknown")
            };
            model.addRow(row);
        }
    }

    private void addPlayer() {
        JTextField nameField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField ageField = new JTextField();

        JComboBox<String> teamCombo = new JComboBox<>();
        for (String name : teamMap.values()) {
            teamCombo.addItem(name);
        }

        Object[] fields = {
            "Name:", nameField,
            "Position:", positionField,
            "Age:", ageField,
            "Team:", teamCombo
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Player", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String teamName = (String) teamCombo.getSelectedItem();
            int teamId = teamMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(teamName))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(0);

            Player p = new Player();
            p.setName(nameField.getText());
            p.setPosition(positionField.getText());
            p.setAge(Integer.parseInt(ageField.getText()));
            p.setTeamId(teamId);

            playerDAO.addPlayer(p);
            loadPlayers();
        }
    }

    private void deletePlayer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int playerId = (int) table.getValueAt(selectedRow, 0);
            playerDAO.deletePlayer(playerId);
            loadPlayers();
        } else {
            JOptionPane.showMessageDialog(null, "Select a player to delete.");
        }
    }
}
