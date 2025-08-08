package com.kath.mybankapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AccountDashboard extends JFrame {

    // <editor-fold desc="AccountDashboard GUI Components">
    private JPanel accountDashboardPanel;
    private JPanel mainPanel;

    //TOP PANEL
    private JPanel topPanel;
    private JPanel balancePanel;
    private JLabel lblBalance;
    private JLabel lblWelcome;

    //CONTENT PANEL
    private JPanel contentPanel;
    private JPanel home;
    private JPanel profilePanel;
    private JPanel cashin;
    private JPanel cashout;
    private JPanel transactions;
    private JPanel settings;
    private JPanel changeMpinFormPanel;
    private JLabel lblProfileName;
    private JLabel lblProfileNumber;
    private JLabel lblProfileEmail;
    private JLabel lblCashInMessage;
    private JLabel lblCashOutMessage;
    private JLabel lblOldMpin;
    private JLabel lblNewMpin;
    private JLabel lblConfirmMpin;
    private JTextField tfCashInAmount;
    private JTextField tfCashOutMobileNumber;
    private JTextField tfCashOutAmount;
    private JPasswordField pfOldMpin;
    private JPasswordField pfNewMpin;
    private JPasswordField pfConfirmMpin;
    private JButton btnCashInCancel;
    private JButton btnCashInSubmit;
    private JButton btnCashOutCancel;
    private JButton btnCashOutSubmit;
    private JButton btnChangeMpin;
    private JButton btnSaveMpin;
    private JScrollPane JScrollPaneRecentTrans;
    private JTable transactionsTable;
    private JTable transactionHistoryTable;

    //NAVIGATION PANEL
    private JPanel navPanel;
    private JPanel homePanel;
    private JPanel cashInPanel;
    private JPanel cashOutPanel;
    private JPanel transactionsPanel;
    private JPanel settingsPanel;
    private JPanel logoutPanel;
    private JLabel lblHome;
    private JLabel lblCashIn;
    private JLabel lblCashOut;
    private JLabel lblTransactions;
    private JLabel lblSettings;
    private JLabel lblLogout;
    private JButton btnHome;
    private JButton btnCashIn;
    private JButton btnCashOut;
    private JButton btnTransactions;
    private JButton btnSettings;
    private JButton btnLogout;

    private User currentUser;
    // </editor-fold>

    public AccountDashboard(User user) {
        this.currentUser = user;

        initializeFrame();
        loadUserProfile();
        setupTransactionTables();
        setupCashInFunctionality();
        setupCashOutFunctionality();
        setupMpinChangeFeature();

        setVisible(true);
    }

    //LAYOUT
    private void initializeFrame() {
        setTitle("MLBB - Account Dashboard");
        setContentPane(accountDashboardPanel);
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/wallet.png"))).getImage());

        accountDashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));

        //Shifting of panels in the content panel area
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        contentPanel.add(home, "home");
        contentPanel.add(cashin, "cashin");
        contentPanel.add(cashout, "cashout");
        contentPanel.add(transactions, "transactions");
        contentPanel.add(settings, "settings");
        cardLayout.show(contentPanel, "home");              //default view

        // NAVIGATION
        navPanel.setLayout(new GridLayout(1, 6, 5, 0));
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "home"));
        btnCashIn.addActionListener(e -> cardLayout.show(contentPanel, "cashin"));
        btnCashOut.addActionListener(e -> cardLayout.show(contentPanel, "cashout"));
        btnTransactions.addActionListener(e -> cardLayout.show(contentPanel, "transactions"));
        btnSettings.addActionListener(e -> cardLayout.show(contentPanel, "settings"));

        // LOGOUT
        btnLogout.addActionListener(e -> {
            dispose();
            new MainDashboard(); // back to main menu
        });
        setVisible(true);
    }

    private void loadUserProfile() {
        lblProfileName.setText("👤 Name      : " + currentUser.getName());
        lblProfileNumber.setText("📱 Number   : " + currentUser.getNumber());
        lblProfileEmail.setText("✉️ Email       : " + currentUser.getEmail());
        lblBalance.setText("₱" + String.format("%,.2f", currentUser.getBalance()));
    }

    private void setupTransactionTables() {
        String[] columns = {"Date", "Details", "Amount"};
        DefaultTableModel recentModel = new DefaultTableModel(null, columns) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        transactionsTable.setModel(recentModel);
        transactionHistoryTable.setModel(new DefaultTableModel(null, columns) {
            public boolean isCellEditable(int row, int col) { return false; }
        });

        styleTransactionTable(transactionsTable);
        styleTransactionTable(transactionHistoryTable);

        // LOAD DATA FROM DATABASE
        loadTransactionsIntoTable(transactionsTable, UserAuthentication.getRecentTransactions(currentUser.getNumber()));
        loadTransactionsIntoTable(transactionHistoryTable, UserAuthentication.getAllTransactions(currentUser.getNumber()));
    }

    private void styleTransactionTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(18);
        table.getTableHeader().setReorderingAllowed(false); // disable column moving
        table.getColumnModel().getColumn(0).setPreferredWidth(100);  // Date
        table.getColumnModel().getColumn(1).setPreferredWidth(160); // Details
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getColumnModel().getColumn(2).setPreferredWidth(50);  // Amount
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

//        //Prevent resizing
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setResizable(false);
//        }
    }

    //CASH-IN
    private void setupCashInFunctionality(){

        btnCashInSubmit.addActionListener(e -> {
            String amountText = tfCashInAmount.getText().trim();

            //If empty input
            if (amountText.isEmpty()) {
                lblCashInMessage.setText("Please enter an amount.");
                lblCashInMessage.setForeground(Color.RED);
                return;
            }
            try {
                double amount = Double.parseDouble(amountText);
                //If amount is 0 or less.
                if (amount <= 0) {
                    lblCashInMessage.setText("Amount must be greater than 0.");
                    lblCashInMessage.setForeground(Color.RED);
                    return;
                }
                // If valid amount, Update balance
                double newBalance = currentUser.getBalance() + amount;
                currentUser.setBalance(newBalance);
                UserAuthentication.updateBalance(currentUser.getNumber(), newBalance);
                UserAuthentication.saveTransaction(currentUser.getNumber(), "Cash In", amount);

                lblBalance.setText("₱" + String.format("%,.2f", newBalance));
                lblCashInMessage.setText("Cash In Successful!");
                lblCashInMessage.setForeground(new Color(0, 128, 0));
                tfCashInAmount.setText("");

                // Log it to home panel (transactionsTable)
                DefaultTableModel dtm = (DefaultTableModel) transactionsTable.getModel();

                String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                dtm.insertRow(0, new Object[]{today, "Cash In", "₱" + String.format("%,.2f", amount)});


                // Reload tables
                loadTransactionsIntoTable(transactionsTable, UserAuthentication.getRecentTransactions(currentUser.getNumber()));
                loadTransactionsIntoTable(transactionHistoryTable, UserAuthentication.getAllTransactions(currentUser.getNumber()));

            } catch (NumberFormatException ex) {
                lblCashInMessage.setText("Invalid amount.");
                lblCashInMessage.setForeground(Color.RED);
            }
        });

        btnCashInCancel.addActionListener(e -> {
            tfCashInAmount.setText("");
            lblCashInMessage.setText("");
        });
    }

    //CASH-OUT
    private void setupCashOutFunctionality(){

        btnCashOutSubmit.addActionListener(e -> {
            String amountText = tfCashOutAmount.getText().trim();
            String recipient = tfCashOutMobileNumber.getText().trim();

            //If empty input
            if (recipient.isEmpty() || amountText.isEmpty()) {
                lblCashOutMessage.setText("All fields are required.");
                lblCashOutMessage.setForeground(Color.RED);
                return;
            }
            // Prevent sending to self
            if (recipient.equals(currentUser.getNumber())) {
                lblCashOutMessage.setText("You cannot send money to your own account.");
                lblCashOutMessage.setForeground(Color.RED);
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                //If amount is 0 or less.
                if (amount <= 0) {
                    lblCashOutMessage.setText("Amount must be greater than 0.");
                    lblCashOutMessage.setForeground(Color.RED);
                    return;
                }
                // Check if recipient exists
                if (!UserAuthentication.userExists(recipient)) {
                    lblCashOutMessage.setText("Recipient not found.");
                    lblCashOutMessage.setForeground(Color.RED);
                    return;
                }
                //Check balance
                if (amount > currentUser.getBalance()) {
                    lblCashOutMessage.setText("Insufficient balance.");
                    lblCashOutMessage.setForeground(Color.RED);
                    return;
                }

                //Proceed with transfer
                boolean success = UserAuthentication.transferMoney(currentUser.getNumber(), recipient, amount);

                if (success) {
                    // Update sender UI
                    currentUser.setBalance(currentUser.getBalance() - amount);
                    lblBalance.setText("₱" + String.format("%,.2f", currentUser.getBalance()));
                    lblCashOutMessage.setText("Transfer successful!");
                    lblCashOutMessage.setForeground(new Color(0, 128, 0));

                    // Log it to home panel (transactionsTable)
                    DefaultTableModel dtm = (DefaultTableModel) transactionsTable.getModel();

                    String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    dtm.insertRow(0, new Object[]{today, "Transfer to " + recipient, "₱" + String.format("%,.2f", amount)});

                    tfCashOutMobileNumber.setText("");
                    tfCashOutAmount.setText("");

                    if (success) {
                        // reload transactions
                        loadTransactionsIntoTable(transactionsTable, UserAuthentication.getRecentTransactions(currentUser.getNumber()));
                        loadTransactionsIntoTable(transactionHistoryTable, UserAuthentication.getAllTransactions(currentUser.getNumber()));
                    }

                } else {
                    lblCashOutMessage.setText("Transfer failed. Try again.");
                    lblCashOutMessage.setForeground(Color.RED);
                }

            } catch (NumberFormatException ex) {
                lblCashOutMessage.setText("Invalid amount.");
                lblCashOutMessage.setForeground(Color.RED);
            }
        });

        btnCashOutCancel.addActionListener(e -> {
            tfCashOutMobileNumber.setText("");
            tfCashOutAmount.setText("");
            lblCashOutMessage.setText("");
        });
    }

    private void loadTransactionsIntoTable(JTable table, ResultSet rs) {

        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String date = rs.getString("date");
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                model.addRow(new Object[]{date, type, "₱" + String.format("%,.2f", amount)});
            }

            rs.getStatement().getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    //SETTINGS - Change MPIN
   private void setupMpinChangeFeature(){

        //Initial state - before clicking Change MPIN button
       lblOldMpin.setVisible(false);
       pfOldMpin.setVisible(false);
       lblNewMpin.setVisible(false);
       pfNewMpin.setVisible(false);
       lblConfirmMpin.setVisible(false);
       pfConfirmMpin.setVisible(false);
       btnSaveMpin.setVisible(false);

       //After clicking MPIN button
       btnChangeMpin.addActionListener(e -> {
           lblOldMpin.setVisible(true);
           pfOldMpin.setVisible(true);
           lblNewMpin.setVisible(true);
           pfNewMpin.setVisible(true);
           lblConfirmMpin.setVisible(true);
           pfConfirmMpin.setVisible(true);
           btnSaveMpin.setVisible(true);
       });

       //validate fields once save button is clicked
       btnSaveMpin.addActionListener(e -> {
           String oldMpin = new String(pfOldMpin.getPassword()).trim();
           String newMpin = new String(pfNewMpin.getPassword()).trim();
           String confirmMpin = new String(pfConfirmMpin.getPassword()).trim();

           if(oldMpin.isEmpty() || newMpin.isEmpty() || confirmMpin.isEmpty()){
               JOptionPane.showMessageDialog(null, "Please fill in all fields.");
               return;
           }
           if(!newMpin.equals(confirmMpin)){
               JOptionPane.showMessageDialog(null, "MPIN and confirmation MPIN do not match.");
               return;
           }
           // 🔒 Validate new MPIN: 4 digits only
           if (!newMpin.matches("\\d{4}")) {
               JOptionPane.showMessageDialog(null, "New MPIN must be exactly 4 digits.");
               return;
           }

           boolean result = UserAuthentication.changeMpin(currentUser.getNumber(),oldMpin, newMpin);
           if(result){
               JOptionPane.showMessageDialog(null, "MPIN successfully changed!");
               pfOldMpin.setText("");
               pfNewMpin.setText("");
               pfConfirmMpin.setText("");
           } else{
               JOptionPane.showMessageDialog(null, "Failed to change MPIN. Please check your old MPIN.");
           }
       });
   }

    public JPanel getMainPanel() {
        return accountDashboardPanel;
    }

    private void createUIComponents() {
        // custom UI creation if needed
    }
}
