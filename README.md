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