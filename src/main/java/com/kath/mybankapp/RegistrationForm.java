package com.kath.mybankapp;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.*;
import java.util.Objects;
import java.util.function.Predicate;

public class RegistrationForm extends JFrame {

    // <editor-fold desc="RegistrationForm GUI Components">
    private JPanel registrationWindowPanel;

    // TOP PANEL
    private JPanel topPanel;
    private JPanel topIconTxtPanel;
    private JPanel topIconPanel;
    private JPanel topTxt1Panel;
    private JPanel topTxt2Panel;
    private JLabel lblIcon;
    private JLabel lblTxt1a;
    private JLabel lblTxt1b;

    // FORM PANEL
    private JPanel formPanel;
    private JPanel formNamePanel;
    private JPanel formEmailPanel;
    private JPanel formNumberPanel;
    private JPanel formMpinPanel;
    private JPanel formConfirmMpinPanel;
    private JLabel lblName;
    private JLabel lblEmail;
    private JLabel lblNumber;
    private JLabel lblMpin;
    private JLabel lblConfirmMpin;
    private JLabel lblNameError;
    private JLabel lblEmailError;
    private JLabel lblNumberError;
    private JLabel lblMpinError;
    private JLabel lblConfirmMpinError;
    private JLabel lblGeneralError;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfNumber;
    private JPasswordField pfMpin;
    private JPasswordField pfConfirmMpin;

    // BUTTONS PANEL
    private JPanel buttonsPanel;
    private JButton btnRegister;
    private JButton btnLoginInstead;
    private JButton btnBack;
    // </editor-fold>

    // CONSTRUCTOR
    public RegistrationForm() {
        setTitle("MLBB - Registration");
        setContentPane(registrationWindowPanel);
        setSize(380, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/wallet.png"))).getImage());

        initComponents();
        setupUIBehavior();
        setVisible(true);
    }

    private void initComponents() {
        btnRegister.addActionListener(e -> handleRegister());
        btnLoginInstead.addActionListener(e -> {
            dispose(); // Close form
            new LoginForm();
        });
        btnBack.addActionListener(e -> {
            dispose(); // Close form
            new MainDashboard();
        });
    }

    // Real-time validation - text and password fields (checks is empty input)
    private void setupFieldValidation(JTextComponent field, JLabel errorLabel, Predicate<String> validator, String errorMsg) {

        field.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                String text = (field instanceof JPasswordField)
                        ? new String(((JPasswordField) field).getPassword()).trim()
                        : field.getText().trim();

                if (text.isEmpty()) {
                    errorLabel.setText("❌ This field is required.");
                    errorLabel.setVisible(true);
                    return;
                }

                boolean isValid = validator.test(text);

                if (isValid) {
                    errorLabel.setVisible(false);
                } else {
                    errorLabel.setText("❌ " + errorMsg);
                    errorLabel.setVisible(true);
                }
            }
        });
    }

    private void setupUIBehavior() {
        setupFieldValidation(tfName, lblNameError, this::isValidName, "Name must contain letters only (min 2 characters).");
        setupFieldValidation(tfEmail, lblEmailError, this::isValidEmail, "Enter a valid email format.");
        setupFieldValidation(tfNumber, lblNumberError, this::isValidNumber, "Mobile number must be exactly 11 digits.");
        setupFieldValidation(pfMpin, lblMpinError, this::isValidMpin, "MPIN must be exactly 4 digits.");

        //Checks if MPIN matches Confirm MPIN
        setupFieldValidation(pfConfirmMpin, lblConfirmMpinError,
                confirm -> isConfirmMpinMatched(new String(pfMpin.getPassword()).trim(), confirm),
                "MPIN and Confirm MPIN do not match!");
    }

    //When Register button is clicked
    private void handleRegister() {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String number = tfNumber.getText().trim();
        String mpin = new String(pfMpin.getPassword()).trim();
        String confirmMpin = new String(pfConfirmMpin.getPassword()).trim();

        // Validate all fields
        if (name.isEmpty() || email.isEmpty() || number.isEmpty() || mpin.isEmpty() || confirmMpin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidName(name)) {
            JOptionPane.showMessageDialog(this, "Invalid full name format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidNumber(number)) {
            JOptionPane.showMessageDialog(this, "Mobile number must be exactly 11 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidMpin(mpin)) {
            JOptionPane.showMessageDialog(this, "MPIN must be exactly 4 digits.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!mpin.equals(confirmMpin)) {
            JOptionPane.showMessageDialog(this, "MPIN and Confirm MPIN do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call database registration logic
        boolean registered = UserAuthentication.register(name, email, number, mpin);

        if (registered) {
            JOptionPane.showMessageDialog(this, "Registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close registration window
            new LoginForm(); // Open login window
        } else {
            JOptionPane.showMessageDialog(this, "User already exists or registration failed.", "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Validation rules for fields
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]{2,}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    private boolean isValidNumber(String number) {
        return number.matches("\\d{11}");
    }

    private boolean isValidMpin(String mpin) {
        return mpin.matches("\\d{4}");
    }

    private boolean isConfirmMpinMatched(String mpin, String confirmMpin) {
        return mpin.equals(confirmMpin);
    }
}
