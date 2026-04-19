package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.UUID;

import db.UserDAO;
import model.User;
import util.PasswordUtil;

public class SimpleServer {

    private static UserDAO dao = new UserDAO();

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // ---------------- ROOT ----------------
        server.createContext("/", (exchange) -> {
            sendResponse(exchange, "Server running!");
        });

        // ---------------- SIGNUP ----------------
        server.createContext("/signup", (exchange) -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String[] parts = body.split(",");

                if (parts.length < 2) {
                    sendResponse(exchange, "Invalid input");
                    return;
                }

                String name = parts[0];
                String password = parts[1];

                String accNo = UUID.randomUUID().toString().substring(0, 8);
                String hashed = PasswordUtil.hash(password);

                User user = new User(name, accNo, hashed);
                dao.saveUser(user);

                sendResponse(exchange, "Signup successful! Account: " + accNo);

            } else {
                sendResponse(exchange, "Use POST");
            }
        });

        // ---------------- LOGIN ----------------
        server.createContext("/login", (exchange) -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String[] parts = body.split(",");

                String acc = parts[0];
                String pass = parts[1];

                User user = dao.getUser(acc);

                if (user != null && PasswordUtil.verify(pass, user.getPassword())) {
                    sendResponse(exchange, "Login successful!");
                } else {
                    sendResponse(exchange, "Invalid credentials");
                }

            } else {
                sendResponse(exchange, "Use POST");
            }
        });

        // ---------------- BALANCE ----------------
        server.createContext("/balance", (exchange) -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                String acc = new String(exchange.getRequestBody().readAllBytes());

                User user = dao.getUser(acc);

                if (user != null) {
                    sendResponse(exchange, String.valueOf(user.getBalance()));
                } else {
                    sendResponse(exchange, "User not found");
                }

            } else {
                sendResponse(exchange, "Use POST");
            }
        });

        // ---------------- DEPOSIT ----------------
        server.createContext("/deposit", (exchange) -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String[] parts = body.split(",");

                String acc = parts[0];
                double amt = Double.parseDouble(parts[1]);

                User user = dao.getUser(acc);

                if (user != null && amt > 0) {
                    user.setBalance(user.getBalance() + amt);
                    dao.updateBalance(acc, user.getBalance());
                    sendResponse(exchange, "Deposit successful!");
                } else {
                    sendResponse(exchange, "Invalid deposit");
                }

            } else {
                sendResponse(exchange, "Use POST");
            }
        });

        // ---------------- WITHDRAW ----------------
        server.createContext("/withdraw", (exchange) -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

                String body = new String(exchange.getRequestBody().readAllBytes());
                String[] parts = body.split(",");

                String acc = parts[0];
                double amt = Double.parseDouble(parts[1]);

                User user = dao.getUser(acc);

                if (user != null && amt > 0 && amt <= user.getBalance()) {
                    user.setBalance(user.getBalance() - amt);
                    dao.updateBalance(acc, user.getBalance());
                    sendResponse(exchange, "Withdraw successful!");
                } else {
                    sendResponse(exchange, "Invalid or insufficient balance");
                }

            } else {
                sendResponse(exchange, "Use POST");
            }
        });

        server.start();
        System.out.println("Server running on http://localhost:8080");
    }

    // ---------------- COMMON RESPONSE ----------------
    private static void sendResponse(HttpExchange exchange, String response) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}