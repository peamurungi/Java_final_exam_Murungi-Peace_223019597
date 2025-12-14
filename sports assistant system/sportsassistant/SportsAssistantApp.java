package com.sportsassistant;

import com.sportsassistant.panels.*;
import javax.swing.*;

public class SportsAssistantApp extends JFrame {

    public SportsAssistantApp() {
        setTitle("Sports Assistant App");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JTabbedPane tabs = new JTabbedPane(); 

        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Players", new PlayerPanel());
        tabs.addTab("Teams", new TeamPanel());
        tabs.addTab("Coaches", new CoachPanel());
        tabs.addTab("Matches", new MatchPanel()); 
        tabs.addTab("Scores", new ScorePanel());
        tabs.addTab("Statistics", new StatisticPanel());

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            LoginPanel loginPanel = new LoginPanel();
            loginPanel.setVisible(true);

           
            loginPanel.setLoginListener(success -> {
                if (success) {
                    loginPanel.dispose(); 
                    SportsAssistantApp app = new SportsAssistantApp();
                    app.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed! Try again.");
                }
            });
        });
    }
}
