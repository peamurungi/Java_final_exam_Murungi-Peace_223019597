package com.sportsassistant.panels;

import com.sportsassistant.dao.ScoreDAO;
import com.sportsassistant.dao.TeamDAO;
import com.sportsassistant.dao.MatchDAO;
import com.sportsassistant.model.Score;
import com.sportsassistant.model.Team;
import com.sportsassistant.model.Match;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScorePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addButton, deleteButton, refreshButton;

    private ScoreDAO scoreDAO;
    private TeamDAO teamDAO;
    private MatchDAO matchDAO;
    private Map<Integer, String> teamMap;
    private Map<Integer, String> matchMap;

    public ScorePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // spacing

        scoreDAO = new ScoreDAO();
        teamDAO = new TeamDAO();
        matchDAO = new MatchDAO();

        // Load teams
        teamMap = new HashMap<>();
        for (Team t : teamDAO.getAllTeams()) {
            teamMap.put(t.getTeamId(), t.getTeamName());
        }

        // Load matches
        matchMap = new HashMap<>();
        for (Match m : matchDAO.getAllMatches()) {
            matchMap.put(
                m.getMatchId(),
                teamMap.getOrDefault(m.getTeam1Id(), "Unknown") + " vs " +
                teamMap.getOrDefault(m.getTeam2Id(), "Unknown")
            );
        }

        // Table setup
        String[] columns = {"Score ID", "Match", "Team", "Points"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        // Improve UI
        table.setRowHeight(25);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setBackground(new Color(230, 230, 230));
        header.setOpaque(true);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addButton = new JButton("Add Score");
        deleteButton = new JButton("Delete Score");
        refreshButton = new JButton("Refresh");

        // Button styling
        Dimension btnSize = new Dimension(120, 30);
        addButton.setPreferredSize(btnSize);
        deleteButton.setPreferredSize(btnSize);
        refreshButton.setPreferredSize(btnSize);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> addScore());
        deleteButton.addActionListener(e -> deleteScore());
        refreshButton.addActionListener(e -> loadScores());

        // Load data
        loadScores();
    }

    private void loadScores() {
        model.setRowCount(0);
        List<Score> scores = scoreDAO.getAllScores();
        for (Score s : scores) {
            model.addRow(new Object[]{
                    s.getScoreId(),
                    matchMap.getOrDefault(s.getMatchId(), "Unknown"),
                    teamMap.getOrDefault(s.getTeamId(), "Unknown"),
                    s.getPoints()
            });
        }
    }

    private void addScore() {
        JComboBox<String> matchCombo = new JComboBox<>();
        for (String mName : matchMap.values()) matchCombo.addItem(mName);

        JComboBox<String> teamCombo = new JComboBox<>();
        for (String tName : teamMap.values()) teamCombo.addItem(tName);

        JTextField pointsField = new JTextField();

        Object[] fields = {
                "Match:", matchCombo,
                "Team:", teamCombo,
                "Points:", pointsField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Add New Score", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Score s = new Score();

            int matchId = matchMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(matchCombo.getSelectedItem()))
                    .map(Map.Entry::getKey).findFirst().orElse(0);

            int teamId = teamMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(teamCombo.getSelectedItem()))
                    .map(Map.Entry::getKey).findFirst().orElse(0);

            s.setMatchId(matchId);
            s.setTeamId(teamId);
            s.setPoints(Integer.parseInt(pointsField.getText()));

            scoreDAO.addScore(s);
            loadScores();
        }
    }

    private void deleteScore() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int scoreId = (int) model.getValueAt(selectedRow, 0);
            scoreDAO.deleteScore(scoreId);
            loadScores();
        } else {
            JOptionPane.showMessageDialog(null, "Select a score to delete.");
        }
    }
}
