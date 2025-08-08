//Stores all the information related to a logged-in user
package com.kath.mybankapp;

public class User {
    private String name;
    private String number;
    private String email;
    private double balance;
    private String role;

    //CONSTRUCTOR
    public User(String name, String number, String email, double balance, String role) {
        this.name = name;
        this.number = number;
        this.email = email;
        this.balance = balance;
        this.role = role;
    }

    //SETTERS & GETTERS
    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRole(){
        return role;
    }

}
