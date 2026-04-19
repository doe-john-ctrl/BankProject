package service;

import java.util.Scanner;
import java.util.UUID;

import db.UserDAO;
import model.User;
import util.PasswordChecker;
import util.PasswordUtil;

public class BankService {

    private UserDAO dao = new UserDAO();
    private Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n1. Signup\n2. Login\n3. Exit");
            String choice = sc.nextLine();

            if (choice.equals("1")) signup();
            else if (choice.equals("2")) login();
            else if (choice.equals("3")) {
                System.out.println("Thank you for using the system!");
                break;
            } else {
                System.out.println("Invalid option");
            }
        }
    }

    private void signup() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        String password;
        while (true) {
            System.out.print("Enter password: ");
            password = sc.nextLine();

            if (PasswordChecker.isStrong(password)) break;
            System.out.println("Weak password. Try again.");
        }

        String accNo = UUID.randomUUID().toString().substring(0, 8);
        String hashed = PasswordUtil.hash(password);

        User user = new User(name, accNo, hashed);
        dao.saveUser(user);

        System.out.println("Account created successfully!");
        System.out.println("Your Account Number: " + accNo);
    }

    private void login() {
        System.out.print("Enter account number: ");
        String acc = sc.nextLine();

        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        User user = dao.getUser(acc);

        if (user != null && PasswordUtil.verify(pass, user.getPassword())) {
            System.out.println("Login successful!");
            userMenu(user);
        } else {
            System.out.println("Invalid credentials");
        }
    }

    private void userMenu(User user) {
        while (true) {
            System.out.println("\n1. Balance\n2. Deposit\n3. Withdraw\n4. Logout");
            String choice = sc.nextLine();

            switch (choice) {

                case "1":
                    System.out.println("Current Balance: " + user.getBalance());
                    break;

                case "2":
                    System.out.print("Enter amount: ");
                    try {
                        double deposit = Double.parseDouble(sc.nextLine());

                        if (deposit <= 0) {
                            System.out.println("Invalid amount");
                            break;
                        }

                        user.setBalance(user.getBalance() + deposit);
                        dao.updateBalance(user.getAccountNumber(), user.getBalance());

                        System.out.println("Deposit successful!");
                        System.out.println("Updated Balance: " + user.getBalance());

                    } catch (Exception e) {
                        System.out.println("Enter a valid number");
                    }
                    break;

                case "3":
                    System.out.print("Enter amount: ");
                    try {
                        double amt = Double.parseDouble(sc.nextLine());

                        if (amt <= 0) {
                            System.out.println("Invalid amount");
                        } else if (amt <= user.getBalance()) {
                            user.setBalance(user.getBalance() - amt);
                            dao.updateBalance(user.getAccountNumber(), user.getBalance());

                            System.out.println("Withdraw successful!");
                            System.out.println("Updated Balance: " + user.getBalance());
                        } else {
                            System.out.println("Insufficient balance");
                        }

                    } catch (Exception e) {
                        System.out.println("Enter a valid number");
                    }
                    break;

                case "4":
                    System.out.println("Logged out successfully!");
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}