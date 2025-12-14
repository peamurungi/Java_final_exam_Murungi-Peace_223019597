package com.sportsassistant.panels;

import com.sportsassistant.dao.TeamDAO;
import com.sportsassistant.model.Team;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class TeamPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addButton, deleteButton, refreshButton;

    private TeamDAO teamDAO = new TeamDAO();

    public TeamPanel() {
        setLayout(new BorderLayout());

        // Columns for table
        String[] columns = {"Team ID", "Team Name", "City", "Founded Year", "Created At"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add Team");
        deleteButton = new JButton("Delete Team");
        refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadTeams();

        // Button actions
        addButton.addActionListener(e -> addTeam());
        deleteButton.addActionListener(e -> deleteTeam());
        refreshButton.addActionListener(e -> loadTeams());
    }

    private void loadTeams() {
        model.setRowCount(0);
        List<Team> teams = teamDAO.getAllTeams();
        for (Team t : teams) {
            Object[] row = {
                    t.getTeamId(),
                    t.getTeamName(),
                    t.getCity(),
                    t.getFoundedYear(),
                    t.getCreatedAt()
            };
            model.addRow(row);
        }
    }

    private void addTeam() {
        JTextField nameField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField foundedYearField = new JTextField();

        Object[] fields = {
                "Team Name:", nameField,
                "City:", cityField,
                "Founded Year:", foundedYearField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Add Team", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Team t = new Team();
            t.setTeamName(nameField.getText());
            t.setCity(cityField.getText());
            t.setFoundedYear(Integer.parseInt(foundedYearField.getText()));

            teamDAO.addTeam(t);
            loadTeams();
        }
    }

    private void deleteTeam() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int teamId = (int) table.getValueAt(selectedRow, 0);
            teamDAO.deleteTeam(teamId);
            loadTeams();
        } else {
            JOptionPane.showMessageDialog(null, "Select a team to delete.");
        }
    }
}
