//Handles login, registration, balance updates, transactions by connecting to database
package com.kath.mybankapp;

import java.sql.*;

public class UserAuthentication {

    // LOGIN VALIDATION
    public static User login(String number, String mpin) {

        try (Connection conn = DBConnection.getConnection()) {                   //try (...) - auto-closes the connection when done.
            String sql = "SELECT * FROM users WHERE number = ? AND pin = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);                 //PreparedStatement - executes precompiled SQL with placeholders (?) — safer, faster, better for user input.
            stmt.setString(1, number);                              //stmt.setString(..) - sets the value of a placeholder (?)
            stmt.setString(2, mpin);

            ResultSet rs = stmt.executeQuery();                                  //ResultSet - holds the data returned by a SQL SELECT query.
            if (rs.next()) {                                                     //rs.next() - moves the cursor to the next row of the query result, and returns true if there is a row.
                String name = rs.getString("name");                   //getString(..) - retrieves data from the current row in the query result.
                String email = rs.getString("email");
                double balance = rs.getDouble("balance");
                String role = rs.getString("role");
                return new User(name, number, email, balance, role);
            }

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());                        //short description of error
        }

        return null;                                                            // ❌ Login failed, return nothing
    }

    // REGISTRATION
    public static boolean register(String name, String email, String number, String mpin) {
        if (userExists(name, email, number)) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (name, email, number, pin, balance) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, number);
            stmt.setString(4, mpin);
            stmt.setDouble(5, 0.0); // default balance

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    //CHECKS IF USER EXISTS DURING REGISTRATION TO AVOID DUPLICATE ACCOUNT
    public static boolean userExists(String name, String email, String number) {

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE name = ? OR email = ? OR number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, number);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }

    }

    // CHECKS IF USER EXISTS - Validate recipient number for cash out, send money, or MPIN reset.
    public static boolean userExists(String number) {

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, number);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    //VERIFICATION DURING RESET MPIN
    public static boolean verifyUser(String number, String name, String email) {

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE number = ? AND name = ? AND email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, number);
            stmt.setString(2, name);
            stmt.setString(3, email);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // found match

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    //ONCE ACCOUNT IS VERIFIED, PROGRAM WILL GENERATE A TEMPORARY MPIN AND WILL UPDATE THE DATABASE
    public static boolean updateMpin(String number, String newMpin) {

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE users SET pin = ? WHERE number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newMpin);
            stmt.setString(2, number);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    // UPDATE USER BALANCE
    public static void updateBalance(String number, double newBalance) {

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE users SET balance = ? WHERE number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newBalance);
            stmt.setString(2, number);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    // SAVE TRANSACTION LOG
    public static void saveTransaction(String number, String type, double amount) {
        int userId = getUserIdByNumber(number);

        if (userId == -1) {
            System.out.println("User ID not found for number: " + number);
            return;
        }
        String insertQuery = "INSERT INTO `transactions` (user_id, user_number, type, amount, date) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setInt(1, userId);
            stmt.setString(2, number);
            stmt.setString(3, type);
            stmt.setDouble(4, amount);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    // MONEY TRANSFER
    public static boolean transferMoney(String senderNumber, String recipientNumber, double amount) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Deduct from sender
            try (PreparedStatement deductStmt = conn.prepareStatement(
                    "UPDATE users SET balance = balance - ? WHERE number = ?")) {
                deductStmt.setDouble(1, amount);
                deductStmt.setString(2, senderNumber);
                deductStmt.executeUpdate();
            }

            // Credit to recipient
            try (PreparedStatement creditStmt = conn.prepareStatement(
                    "UPDATE users SET balance = balance + ? WHERE number = ?")) {
                creditStmt.setDouble(1, amount);
                creditStmt.setString(2, recipientNumber);
                creditStmt.executeUpdate();
            }

            // Insert sender and recipient transaction logs
            String logSql = "INSERT INTO `transactions` (user_id, user_number, type, amount, date) VALUES (?, ?, ?, ?, NOW())";
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                int senderId = getUserIdByNumber(senderNumber);
                int recipientId = getUserIdByNumber(recipientNumber);

                // Sender transaction
                logStmt.setInt(1, senderId);
                logStmt.setString(2, senderNumber);
                logStmt.setString(3, "Transfer to " + recipientNumber);
                logStmt.setDouble(4, amount);
                logStmt.executeUpdate();

                // Recipient transaction
                logStmt.setInt(1, recipientId);
                logStmt.setString(2, recipientNumber);
                logStmt.setString(3, "Received from " + senderNumber);
                logStmt.setDouble(4, amount);
                logStmt.executeUpdate();
            }

            conn.commit(); // Success
            return true;

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    public static boolean changeMpin(String number, String oldMpin, String newMpin) {

        try (Connection conn = DBConnection.getConnection()) {
            //Check if old MPIN is correct
            String checkSql = "SELECT * FROM users WHERE number = ? AND pin = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, number);
            checkStmt.setString(2, oldMpin);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                //proceed to update
                String updateSql = "UPDATE users SET pin = ? WHERE number = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, newMpin);
                updateStmt.setString(2, number);

                int rows = updateStmt.executeUpdate();
                return rows > 0;

            } else {
                return false; //old mpin does not match
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }

    // GET ALL TRANSACTIONS
    public static ResultSet getAllTransactions(String number) {

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT date, type, amount FROM `transactions` WHERE user_number = ? ORDER BY id DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, number);
            return stmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return null;
    }

    // GET RECENT 5 TRANSACTIONS
    public static ResultSet getRecentTransactions(String number) {

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT date, type, amount FROM `transactions` WHERE user_number = ? ORDER BY id DESC LIMIT 5";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, number);
            return stmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return null;
    }

    public static int getUserIdByNumber(String number) {
        int userId = -1;
        String query = "SELECT id FROM users WHERE number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, number);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
