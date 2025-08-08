package com.kath.mybankapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Objects;

public class AdminDashboard extends JFrame  {
    private JPanel mainPanel;
    private JComboBox<String> viewOptionsComboBox;
    private JComboBox<String> userComboBox;
    private JTable table;
    private JButton btnLogout;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/wallet.png"))).getImage());

        btnLogout.addActionListener(e -> {
            dispose(); // ✅ Close Admin window
            new MainDashboard(); // ✅ Opens new JFrame (main dashboard)
        });


        viewOptionsComboBox.addItem("View all users");
        viewOptionsComboBox.addItem("View all transactions");
        viewOptionsComboBox.addItem("View transactions per user");

        userComboBox.setVisible(false);

        viewOptionsComboBox.addActionListener(e -> {
            String selectedOption = (String) viewOptionsComboBox.getSelectedItem();

            if ("View all transactions".equals(selectedOption)) {
                userComboBox.setVisible(false);
                loadAllTransactions();
            } else if ("View all users".equals(selectedOption)) {
                userComboBox.setVisible(false);
                loadAllUsers();
            } else if ("View transactions per user".equals(selectedOption)) {
                userComboBox.setVisible(true);
                populateUserComboBox();
            }
        });

        userComboBox.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            if (selectedUser != null) {
                loadTransactionsForUser(selectedUser);
            }
        });

        loadAllUsers(); // Default view
        setVisible(true);

    }

    private void loadAllTransactions() {
        String query = """
            SELECT t.date, u.name, t.type, t.amount 
            FROM transactions t 
            JOIN users u ON t.user_id = u.id 
            ORDER BY t.date DESC
        """;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Date", "Name", "Type", "Amount"});

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("date"),
                        rs.getString("name"),
                        rs.getString("type"),
                        "₱" + rs.getString("amount")
                });
            }

            table.setModel(model);
            resizeColumnWidths();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load transactions");
        }
    }

    private void loadAllUsers() {
        String query = "SELECT name, email, number, balance FROM users ORDER BY name ASC";

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Name", "Email", "Number", "Balance"});

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("number"),
                        "₱" + rs.getString("balance")
                });
            }

            table.setModel(model);
            resizeColumnWidths();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load users");
        }
    }

    private void loadTransactionsForUser(String fullName) {
        String query = """
            SELECT t.date, t.type, t.amount 
            FROM transactions t 
            JOIN users u ON t.user_id = u.id 
            WHERE u.name = ? 
            ORDER BY t.date DESC
        """;

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Date", "Type", "Amount"});

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fullName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("date"),
                        rs.getString("type"),
                        "₱" + rs.getString("amount")
                });
            }

            table.setModel(model);
            resizeColumnWidths();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load user transactions");
        }
    }

    private void populateUserComboBox() {
        userComboBox.removeAllItems();

        String query = "SELECT name FROM users WHERE role != 'admin' ORDER BY name ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                userComboBox.addItem(rs.getString("name"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load user list");
        }
    }

    private void resizeColumnWidths() {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
