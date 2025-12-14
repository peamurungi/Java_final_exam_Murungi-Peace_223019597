package com.sportsassistant.panels;

import com.sportsassistant.dao.MatchDAO;
import com.sportsassistant.dao.TeamDAO;
import com.sportsassistant.model.Match;
import com.sportsassistant.model.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addButton, deleteButton, refreshButton;

    private MatchDAO matchDAO;
    private TeamDAO teamDAO;
    private Map<Integer, String> teamMap;

    public MatchPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        matchDAO = new MatchDAO();
        teamDAO = new TeamDAO();
        teamMap = new HashMap<>();

        List<Team> teams = teamDAO.getAllTeams();
        for (Team t : teams) {
            teamMap.put(t.getTeamId(), t.getTeamName());
        }

        String[] columns = {"Match ID", "Team 1", "Team 2", "Date", "Location"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // Table Styling
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setBackground(new Color(230, 230, 230));
        header.setOpaque(true);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton = new JButton("Add Match");
        deleteButton = new JButton("Delete Match");
        refreshButton = new JButton("Refresh");

        Dimension btnSize = new Dimension(120, 30);
        addButton.setPreferredSize(btnSize);
        deleteButton.setPreferredSize(btnSize);
        refreshButton.setPreferredSize(btnSize);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> addMatch());
        deleteButton.addActionListener(e -> deleteMatch());
        refreshButton.addActionListener(e -> loadMatches());

        // Initial Load
        loadMatches();
    }

    private void loadMatches() {
        model.setRowCount(0);
        List<Match> matches = matchDAO.getAllMatches();

        for (Match m : matches) {
            model.addRow(new Object[]{
                    m.getMatchId(),
                    teamMap.getOrDefault(m.getTeam1Id(), "Unknown"),
                    teamMap.getOrDefault(m.getTeam2Id(), "Unknown"),
                    m.getMatchDate(),
                    m.getLocation()
            });
        }
    }

    private void addMatch() {
        JComboBox<String> team1Combo = new JComboBox<>();
        JComboBox<String> team2Combo = new JComboBox<>();

        for (String name : teamMap.values()) {
            team1Combo.addItem(name);
            team2Combo.addItem(name);
        }

        JTextField dateField = new JTextField();
        JTextField locationField = new JTextField();

        Object[] fields = {
                "Team 1:", team1Combo,
                "Team 2:", team2Combo,
                "Date (YYYY-MM-DD):", dateField,
                "Location:", locationField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Add New Match", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            Match m = new Match();

            int team1Id = teamMap.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(team1Combo.getSelectedItem()))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(0);

            int team2Id = teamMap.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(team2Combo.getSelectedItem()))
                    .map(Map.Entry::getKey)
                    .findFirst().orElse(0);

            m.setTeam1Id(team1Id);
            m.setTeam2Id(team2Id);

            m.setMatchDate(Date.valueOf(dateField.getText()));
            m.setLocation(locationField.getText());

            matchDAO.addMatch(m);
            loadMatches();
        }
    }

    private void deleteMatch() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            int matchId = (int) model.getValueAt(selectedRow, 0);
            matchDAO.deleteMatch(matchId);
            loadMatches();
        } else {
            JOptionPane.showMessageDialog(null, "Select a match to delete.");
        }
    }
}
