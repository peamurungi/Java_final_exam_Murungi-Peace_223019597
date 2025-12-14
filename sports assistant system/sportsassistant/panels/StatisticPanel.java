package com.sportsassistant.panels;

import com.sportsassistant.dao.StatisticDAO;
import com.sportsassistant.dao.PlayerDAO;
import com.sportsassistant.model.Statistic;
import com.sportsassistant.model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class StatisticPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn, editBtn, deleteBtn, refreshBtn, generateReportBtn;
    private StatisticDAO dao = new StatisticDAO();
    private PlayerDAO playerDAO = new PlayerDAO();
    private Map<Integer, String> playerNameMap = new HashMap<>();

    public StatisticPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        model = new DefaultTableModel(new String[]{"ID", "Player Name", "Matches Played", "Goals", "Assists"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(22);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(BorderFactory.createTitledBorder("Player Statistics"));
        add(pane, BorderLayout.CENTER);

        // Buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        addBtn = new JButton("Add");
        editBtn = new JButton("Edit");
        deleteBtn = new JButton("Delete");
        refreshBtn = new JButton("Refresh");
        generateReportBtn = new JButton("Generate Report (PDF)");

 
        java.awt.Font btnFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13);
        for (JButton b : new JButton[]{addBtn, editBtn, deleteBtn, refreshBtn, generateReportBtn}) {
            b.setFont(btnFont);
            b.setPreferredSize(new Dimension(150, 32));
            b.setFocusPainted(false);
            b.setBackground(new Color(235, 235, 245));
            panel.add(Box.createRigidArea(new Dimension(6,0)));
            panel.add(b);
        }
        panel.add(Box.createHorizontalGlue());
        add(panel, BorderLayout.SOUTH);

        // Actions
        loadData();
        refreshBtn.addActionListener(e -> loadData());

        addBtn.addActionListener(e -> {
            
            loadPlayerNames();

            JComboBox<String> playerCombo = new JComboBox<>();
            for (Map.Entry<Integer, String> entry : playerNameMap.entrySet()) {
                playerCombo.addItem(entry.getValue() + " (ID: " + entry.getKey() + ")");
            }

            JTextField matches = new JTextField();
            JTextField goals = new JTextField();
            JTextField assists = new JTextField();
            Object[] msg = {"Select Player:", playerCombo, "Matches Played:", matches, "Goals:", goals, "Assists:", assists};

            if (JOptionPane.showConfirmDialog(this, msg, "Add Statistic", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    String selected = (String) playerCombo.getSelectedItem();
                    int playerId = extractPlayerIdFromString(selected);

                    Statistic s = new Statistic();
                    s.setPlayerId(playerId);
                    s.setMatchesPlayed(Integer.parseInt(matches.getText()));
                    s.setGoals(Integer.parseInt(goals.getText()));
                    s.setAssists(Integer.parseInt(assists.getText()));
                    dao.addStatistic(s);
                    loadData();
                    JOptionPane.showMessageDialog(this, "Statistic added successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error adding statistic: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel >= 0) {
                int id = (int) model.getValueAt(sel, 0);
                String currentPlayerName = model.getValueAt(sel, 1).toString();

             
                loadPlayerNames();

                JComboBox<String> playerCombo = new JComboBox<>();
                int selectedIndex = 0;
                int index = 0;
                for (Map.Entry<Integer, String> entry : playerNameMap.entrySet()) {
                    String item = entry.getValue() + " (ID: " + entry.getKey() + ")";
                    playerCombo.addItem(item);
                    if (entry.getValue().equals(currentPlayerName)) {
                        selectedIndex = index;
                    }
                    index++;
                }
                playerCombo.setSelectedIndex(selectedIndex);

                JTextField matches = new JTextField(model.getValueAt(sel, 2).toString());
                JTextField goals = new JTextField(model.getValueAt(sel, 3).toString());
                JTextField assists = new JTextField(model.getValueAt(sel, 4).toString());
                Object[] msg = {"Select Player:", playerCombo, "Matches Played:", matches, "Goals:", goals, "Assists:", assists};

                if (JOptionPane.showConfirmDialog(this, msg, "Edit Statistic", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        String selected = (String) playerCombo.getSelectedItem();
                        int playerId = extractPlayerIdFromString(selected);

                        Statistic s = new Statistic(id, playerId, Integer.parseInt(matches.getText()), Integer.parseInt(goals.getText()), Integer.parseInt(assists.getText()));
                        dao.updateStatistic(s);
                        loadData();
                        JOptionPane.showMessageDialog(this, "Statistic updated successfully!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error updating statistic: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a statistic to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteBtn.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel >= 0) {
                int id = (int) model.getValueAt(sel, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete selected statistic?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try {
                        dao.deleteStatistic(id);
                        loadData();
                        JOptionPane.showMessageDialog(this, "Statistic deleted successfully!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error deleting statistic: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a statistic to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        generateReportBtn.addActionListener(e -> generateReport());
    }

    private void loadPlayerNames() {
        try {
            playerNameMap.clear();
            List<Player> players = playerDAO.getAllPlayers();
            for (Player p : players) {
                playerNameMap.put(p.getPlayerId(), p.getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading players: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int extractPlayerIdFromString(String playerString) {
        int idStart = playerString.lastIndexOf("ID: ") + 4;
        int idEnd = playerString.lastIndexOf(")");
        return Integer.parseInt(playerString.substring(idStart, idEnd));
    }

    private void loadData() {
        try {
            loadPlayerNames();

            List<Statistic> list = dao.getAllStatistics();

            model.setRowCount(0);
            for (Statistic s : list) {
                String playerName = playerNameMap.getOrDefault(s.getPlayerId(), "Unknown Player");
                model.addRow(new Object[]{
                        s.getStatisticId(),
                        playerName,
                        s.getMatchesPlayed(),
                        s.getGoals(),
                        s.getAssists()
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateReport() {
        String[] reportTypes = {
                "All Statistics Summary",
                "Top Performers (Scorers & Assisters)",
                "Top Scorers Report",
                "Top Assisters Report",
                "Player Performance Report"
        };

        String selectedType = (String) JOptionPane.showInputDialog(
                this,
                "Select Report Type:",
                "Generate PDF Report",
                JOptionPane.QUESTION_MESSAGE,
                null,
                reportTypes,
                reportTypes[0]
        );

        if (selectedType == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        String filename = selectedType.replace(" ", "_") + "_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        fileChooser.setSelectedFile(new File(filename));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }

            try {
                switch (selectedType) {
                    case "All Statistics Summary":
                        generateAllStatisticsPDF(file);
                        break;
                    case "Top Performers (Scorers & Assisters)":
                        generateTopPerformersPDF(file);
                        break;
                    case "Top Scorers Report":
                        generateTopScorersPDF(file);
                        break;
                    case "Top Assisters Report":
                        generateTopAssistersPDF(file);
                        break;
                    case "Player Performance Report":
                        generatePlayerPerformancePDF(file);
                        break;
                }

                JOptionPane.showMessageDialog(this,
                        "PDF Report generated successfully!\nSaved to: " + file.getAbsolutePath(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                int open = JOptionPane.showConfirmDialog(this,
                        "Would you like to open the PDF report?",
                        "Open Report",
                        JOptionPane.YES_NO_OPTION);

                if (open == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(file); // Desktop is imported above
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error generating PDF report: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generateAllStatisticsPDF(File file) throws Exception {
        List<Statistic> statistics = dao.getAllStatistics();
        loadPlayerNames();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
        Paragraph title = new Paragraph("SPORTS ASSISTANT SYSTEM\nALL STATISTICS REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        
        com.itextpdf.text.Font dateFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
        Paragraph date = new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), dateFont);
        date.setAlignment(Element.ALIGN_CENTER);
        date.setSpacingAfter(12);
        document.add(date);

       
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2, 1.5f, 1.5f});

       
        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        PdfPCell[] headers = {
                new PdfPCell(new Phrase("ID", headerFont)),
                new PdfPCell(new Phrase("Player Name", headerFont)),
                new PdfPCell(new Phrase("Matches", headerFont)),
                new PdfPCell(new Phrase("Goals", headerFont)),
                new PdfPCell(new Phrase("Assists", headerFont))
        };

        for (PdfPCell cell : headers) {
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

       
        com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL);
        int totalMatches = 0, totalGoals = 0, totalAssists = 0;

        for (Statistic s : statistics) {
            String playerName = playerNameMap.getOrDefault(s.getPlayerId(), "Unknown");
            table.addCell(new Phrase(String.valueOf(s.getStatisticId()), dataFont));
            table.addCell(new Phrase(playerName, dataFont));
            table.addCell(new Phrase(String.valueOf(s.getMatchesPlayed()), dataFont));
            table.addCell(new Phrase(String.valueOf(s.getGoals()), dataFont));
            table.addCell(new Phrase(String.valueOf(s.getAssists()), dataFont));

            totalMatches += s.getMatchesPlayed();
            totalGoals += s.getGoals();
            totalAssists += s.getAssists();
        }

        document.add(table);

      
        Paragraph summary = new Paragraph("\n\nSUMMARY\n", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD));
        summary.add(new Chunk("Total Records: " + statistics.size() + "\n", dataFont));
        summary.add(new Chunk("Total Matches: " + totalMatches + "\n", dataFont));
        summary.add(new Chunk("Total Goals: " + totalGoals + "\n", dataFont));
        summary.add(new Chunk("Total Assists: " + totalAssists + "\n", dataFont));
        if (statistics.size() > 0) {
            summary.add(new Chunk(String.format("Average Goals per Player: %.2f\n", (double) totalGoals / statistics.size()), dataFont));
            summary.add(new Chunk(String.format("Average Assists per Player: %.2f\n", (double) totalAssists / statistics.size()), dataFont));
        }
        document.add(summary);

        document.close();
    }

    private void generateTopPerformersPDF(File file) throws Exception {
        List<Statistic> statistics = dao.getAllStatistics();
        loadPlayerNames();

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD, BaseColor.BLUE);
        Paragraph title = new Paragraph("TOP PERFORMERS REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        com.itextpdf.text.Font subtitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.ITALIC);
        Paragraph subtitle = new Paragraph("Top Scorers and Top Assisters", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(12);
        document.add(subtitle);

        // Scorers
        Paragraph scorersTitle = new Paragraph("\nTOP 10 SCORERS", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD, BaseColor.RED));
        scorersTitle.setSpacingBefore(6);
        scorersTitle.setSpacingAfter(6);
        document.add(scorersTitle);

        List<Statistic> topScorers = statistics.stream()
                .sorted((s1, s2) -> Integer.compare(s2.getGoals(), s1.getGoals()))
                .limit(10)
                .collect(Collectors.toList());

        PdfPTable scorersTable = new PdfPTable(5);
        scorersTable.setWidthPercentage(100);
        scorersTable.setWidths(new float[]{1, 3, 1.5f, 2, 2});

        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        String[] scorerHeaders = {"Rank", "Player Name", "Goals", "Matches", "Goals/Match"};
        for (String h : scorerHeaders) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(BaseColor.RED);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            scorersTable.addCell(cell);
        }

        com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL);
        int rank = 1;
        for (Statistic s : topScorers) {
            String playerName = playerNameMap.getOrDefault(s.getPlayerId(), "Unknown");
            double goalsPerMatch = s.getMatchesPlayed() > 0 ? (double) s.getGoals() / s.getMatchesPlayed() : 0.0;

            scorersTable.addCell(new Phrase(String.valueOf(rank++), dataFont));
            scorersTable.addCell(new Phrase(playerName, dataFont));
            scorersTable.addCell(new Phrase(String.valueOf(s.getGoals()), dataFont));
            scorersTable.addCell(new Phrase(String.valueOf(s.getMatchesPlayed()), dataFont));
            scorersTable.addCell(new Phrase(String.format("%.2f", goalsPerMatch), dataFont));
        }

        document.add(scorersTable);

       
        Paragraph assistersTitle = new Paragraph("\n\nTOP 10 ASSISTERS", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD, new BaseColor(0, 128, 0)));
        assistersTitle.setSpacingBefore(8);
        assistersTitle.setSpacingAfter(6);
        document.add(assistersTitle);

        List<Statistic> topAssisters = statistics.stream()
                .sorted((s1, s2) -> Integer.compare(s2.getAssists(), s1.getAssists()))
                .limit(10)
                .collect(Collectors.toList());

        PdfPTable assistersTable = new PdfPTable(5);
        assistersTable.setWidthPercentage(100);
        assistersTable.setWidths(new float[]{1, 3, 1.5f, 2, 2});

        String[] assisterHeaders = {"Rank", "Player Name", "Assists", "Matches", "Assists/Match"};
        for (String h : assisterHeaders) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new BaseColor(0, 128, 0));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            assistersTable.addCell(cell);
        }

        rank = 1;
        for (Statistic s : topAssisters) {
            String playerName = playerNameMap.getOrDefault(s.getPlayerId(), "Unknown");
            double assistsPerMatch = s.getMatchesPlayed() > 0 ? (double) s.getAssists() / s.getMatchesPlayed() : 0.0;

            assistersTable.addCell(new Phrase(String.valueOf(rank++), dataFont));
            assistersTable.addCell(new Phrase(playerName, dataFont));
            assistersTable.addCell(new Phrase(String.valueOf(s.getAssists()), dataFont));
            assistersTable.addCell(new Phrase(String.valueOf(s.getMatchesPlayed()), dataFont));
            assistersTable.addCell(new Phrase(String.format("%.2f", assistsPerMatch), dataFont));
        }

        document.add(assistersTable);

        document.close();
    }

    private void generateTopScorersPDF(File file) throws Exception {
        List<Statistic> statistics = dao.getAllStatistics();
        loadPlayerNames();
        statistics.sort((s1, s2) -> Integer.compare(s2.getGoals(), s1.getGoals()));

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
        Paragraph title = new Paragraph("TOP SCORERS REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(12);
        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        String[] headers = {"Rank", "Player Name", "Goals", "Matches", "Goals/Match"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL);
        int rank = 1;
        int limit = Math.min(20, statistics.size());

        for (int i = 0; i < limit; i++) {
            Statistic s = statistics.get(i);
            String playerName = playerNameMap.getOrDefault(s.getPlayerId(), "Unknown");
            double goalsPerMatch = s.getMatchesPlayed() > 0 ? (double) s.getGoals() / s.getMatchesPlayed() : 0.0;

            table.addCell(new Phrase(String.valueOf(rank++), dataFont));
            table.addCell(new Phrase(playerName, dataFont));
            table.addCell(new Phrase(String.valueOf(s.getGoals()), dataFont));
            table.addCell(new Phrase(String.valueOf(s.getMatchesPlayed()), dataFont));
            table.addCell(new Phrase(String.format("%.2f", goalsPerMatch), dataFont));
        }

        document.add(table);
        document.close();
    }

    private void generateTopAssistersPDF(File file) throws Exception {
        List<Statistic> statistics = dao.getAllStatistics();
        loadPlayerNames();
        statistics.sort((s1, s2) -> Integer.compare(s2.getAssists(), s1.getAssists()));

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
        Paragraph title = new Paragraph("TOP ASSISTERS REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(12);
        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);

        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        String[] headers = {"Rank", "Player Name", "Assists", "Matches", "Assists/Match"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL);
        int rank = 1;
        int limit = Math.min(20, statistics.size());

        for (int i = 0; i < limit; i++) {
            Statistic s = statistics.get(i);
            String playerName = playerNameMap.getOrDefault(s.getPlayerId(), "Unknown");
            double assistsPerMatch = s.getMatchesPlayed() > 0 ? (double) s.getAssists() / s.getMatchesPlayed() : 0.0;

            table.addCell(new Phrase(String.valueOf(rank++), dataFont));
            table.addCell(new Phrase(playerName, dataFont));
            table.addCell(new Phrase(String.valueOf(s.getAssists()), dataFont));
            table.addCell(new Phrase(String.valueOf(s.getMatchesPlayed()), dataFont));
            table.addCell(new Phrase(String.format("%.2f", assistsPerMatch), dataFont));
        }

        document.add(table);
        document.close();
    }

    private void generatePlayerPerformancePDF(File file) throws Exception {
        List<Statistic> statistics = dao.getAllStatistics();
        loadPlayerNames();
        statistics.sort((s1, s2) -> {
            int total1 = s1.getGoals() + s1.getAssists();
            int total2 = s2.getGoals() + s2.getAssists();
            return Integer.compare(total2, total1);
        });

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
        Paragraph title = new Paragraph("PLAYER PERFORMANCE REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(12);
        document.add(title);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 2, 1.5f, 1.5f, 2, 2});

        com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        String[] headers = {"Player Name", "Matches", "Goals", "Assists", "Total", "Contrib/Match"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        com.itextpdf.text.Font dataFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL);
        for (Statistic s : statistics) {
            String playerName = playerNameMap.getOrDefault(s.getPlayerId(), "Unknown");
            int totalContributions = s.getGoals() + s.getAssists();
            double contribPerMatch = s.getMatchesPlayed() > 0 ? (double) totalContributions / s.getMatchesPlayed() : 0.0;

            table.addCell(new Phrase(playerName, dataFont));
            table.addCell(new Phrase(String.valueOf(s.getMatchesPlayed()), dataFont));
            table.addCell(new Phrase(String.valueOf(s.getGoals()), dataFont));
            table.addCell(new Phrase(String.valueOf(s.getAssists()), dataFont));
            table.addCell(new Phrase(String.valueOf(totalContributions), dataFont));
            table.addCell(new Phrase(String.format("%.2f", contribPerMatch), dataFont));
        }

        document.add(table);
        document.close();
    }
}
