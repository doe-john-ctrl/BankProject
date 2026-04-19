sql code 

CREATE DATABASE banksdatabase;
USE banksdatabase;

CREATE TABLE users (
    account_number VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    password VARCHAR(255),
    balance DOUBLE
);

INSERT INTO users VALUES ('12345', 'Test User', 'abc123', 1000);
SELECT * FROM users;

run command

javac -cp ".;jbcrypt-0.4.jar;mysql-connector-j-9.6.0.jar" server/*.java service/*.java db/*.java model/*.java util/*.java
java -cp ".;jbcrypt-0.4.jar;mysql-connector-j-9.6.0.jar" server.SimpleServer
