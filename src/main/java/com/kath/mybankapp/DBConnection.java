//Handles database connection: from the Java program to the MySQL Database
package com.kath.mybankapp;

import java.sql.Connection;          //Represents the active connection to the database.
import java.sql.DriverManager;       //Creates the connection.
import java.sql.SQLException;        //Handles errors during database operations

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bankappdb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";            //XAMPP MYSQL PASSWORD

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,DB_USERNAME,DB_PASSWORD);
    }
}
