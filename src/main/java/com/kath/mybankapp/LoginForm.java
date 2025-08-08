package com.kath.mybankapp;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LoginForm extends JFrame {

    // <editor-fold desc="LoginForm GUI Components">
    private JPanel loginWindowPanel;

    // Logo Panel
    private JPanel logoPanel;
    private JPanel loginLogoPanel;
    private JPanel loginTxtPanel;
    private JLabel loginLblIcon;
    private JLabel loginLblTxt;

    // Credential Panel
    private JPanel loginCredentials;
    private JPanel mobileNumberPanel;
    private JPanel mpinPanel;
    private JTextField tfMobileNumber;
    private JPasswordField pfMpin;

    // Button Panel
    private JPanel btnLoginPanel;
    private JPanel loginBtnLoginPanel;
    private JPanel loginBtnForgotMpin;
    private JPanel loginSignUpPanel;
    private JButton btnLogin;
    private JButton btnForgotMpin;
    private JButton btnLoginBack;
    private JButton btnSignUpInstead;
    // </editor-fold>

    public LoginForm() {
        setTitle("MLBB - Login");
        setContentPane(loginWindowPanel);
        setSize(380, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/wallet.png"))).getImage());

        setupUIBehavior();
        setVisible(true);

    }

    // LOGIN UI BEHAVIOR
    private void setupUIBehavior() {

        // Login button
        btnLogin.addActionListener(e -> performLogin());

        // Forgot MPIN
        btnForgotMpin.addActionListener(e -> handleForgotMpin());

        // SignUpInstead link
        styleLinkButton(btnSignUpInstead);
        btnSignUpInstead.addActionListener(e -> {
            dispose();
            new RegistrationForm();
        });

        // Back button (from LoginForm to MainDashboard)
        btnLoginBack.addActionListener(e -> {
            setVisible(false);
            new MainDashboard();
        });
    }

    // Customize SignUp link
    private void styleLinkButton(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // PERFORM LOGIN
    private void performLogin() {
        String number = tfMobileNumber.getText().trim();
        String mpin = String.valueOf(pfMpin.getPassword()).trim();

        if (number.isEmpty() || mpin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill in both fields.");
            return;
        }
        User user = UserAuthentication.login(number, mpin);

        if (user != null) {
            dispose(); // Close the LoginForm window

            if (user.getRole().equalsIgnoreCase("admin")) {
                new AdminDashboard(); // This already has its own JFrame

            } else {
                try{
                new AccountDashboard(user); // Use it directly
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "❌ Failed to open user dashboard.\n" + ex.getMessage());
                }
            }


            JOptionPane.showMessageDialog(null,
                    "<html><span style='font-size:12px; color:green;'>&#10004;</span> Login successful</html>",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {
            JOptionPane.showMessageDialog(this, "❌ Invalid number or MPIN.");
            pfMpin.setText("");
        }
    }

    //RESET MPIN
    private void handleForgotMpin(){

        String number = JOptionPane.showInputDialog(this, "Enter your account number:","Reset MPIN",JOptionPane.QUESTION_MESSAGE);
            if (number == null || number.trim().isEmpty()) return;

        String name = JOptionPane.showInputDialog(this, "Enter your registered name:","Reset MPIN",JOptionPane.QUESTION_MESSAGE);
            if(name == null || name.trim().isEmpty()) return;

        String email = JOptionPane.showInputDialog(this, "Enter your registered email:","Reset MPIN",JOptionPane.QUESTION_MESSAGE);
            if(email == null || email.trim().isEmpty()) return;

        boolean verified = UserAuthentication.verifyUser(number.trim(), name.trim(), email.trim());

        if (verified) {
            String newMpin = generateRandomMpin();
            boolean updated = UserAuthentication.updateMpin(number.trim(), newMpin);

            if (updated) {
                JOptionPane.showMessageDialog(this,
                        "Your MPIN has been reset.\nNew MPIN: " + newMpin,
                        "Reset Successful", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to update MPIN. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this,
                    "Verification failed. Details do not match our records.",
                    "Verification Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    //TEMPORARY MPIN, WILL BE GENERATED ONCE VERIFICATION IS COMPLETED
    private String generateRandomMpin() {
        int mpin = (int)(1000 + Math.random() * 9000); // Random 4-digit
        return String.valueOf(mpin);
    }
}
