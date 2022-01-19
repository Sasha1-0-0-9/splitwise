CREATE TABLE accounts
(
    id SERIAL PRIMARY KEY,
    email VARCHAR (50),
    phone_number VARCHAR(12) REFERENCES contacts(phone_number),
    encrypted_password VARCHAR(100)
);