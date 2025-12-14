package com.sportsassistant.panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// LoginPanel now uses a listener to notify when login is successful
public class LoginPanel extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // Listener interface
    public interface LoginListener {
        void onLoginResult(boolean success);
    }

    private LoginListener loginListener;

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public LoginPanel() {
        setTitle("Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel()); // empty
        add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Example login check (replace with real authentication)
            boolean success = username.equals("admin") && password.equals("1234");

            if (loginListener != null) {
                loginListener.onLoginResult(success);
            }
        });
    }
}
