//Main dashboard (Welcome dashboard when the app opens)
package com.kath.mybankapp;

import javax.swing.*;
import java.util.*;

public class MainDashboard extends JFrame {

    // <editor-fold desc="Main Dashboard GUI Components">
    private JPanel mainDashboardPanel;
    private JPanel logoPanelMain;
    private JPanel phrasePanelMain;
    private JPanel buttonsPanelMain;
    private JLabel lblLogo;
    private JLabel lblPhrase;
    private JButton btnSignUp;
    private JButton btnLogin;
    // </editor-fold>

    // CONSTRUCTOR
    MainDashboard(){
        setTitle("MLBB - Main Dashboard");
        setContentPane(mainDashboardPanel);
        setSize(380, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/wallet.png"))).getImage());

        btnLogin.setFocusable(false);
        btnSignUp.setFocusable(false);

        // Go to Registration Window
        btnSignUp.addActionListener(e -> {
            setVisible(false);                          // Hide the main dashboard
            new RegistrationForm();          // 'this' = MainDashboard
        });

        // Go to Login Window
        btnLogin.addActionListener(e -> {
            setVisible(false);                         // Hide the main dashboard
            new LoginForm();
        });

        setVisible(true);
    }
    public JPanel getMainDashboardPanel() {
        return mainDashboardPanel;
    }

}
