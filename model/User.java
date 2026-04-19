package model;

public class User {
    private String name;
    private String accountNumber;
    private String password;
    private double balance;

    public User(String name, String accountNumber, String password) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 0;
    }

    // ✅ GETTERS
    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    // ✅ SETTER
    public void setBalance(double balance) {
        this.balance = balance;
    }
}