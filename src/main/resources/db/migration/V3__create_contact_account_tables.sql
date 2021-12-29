CREATE TABLE contacts
(
    PhoneNumber CHARACTER VARYING(12) PRIMARY KEY,
    Name CHARACTER VARYING(30),
    Balance DOUBLE PRECISION
);

CREATE TABLE accounts
(
    Id SERIAL PRIMARY KEY,
    Email CHARACTER VARYING(50),
    PhoneNumber CHARACTER VARYING(12) REFERENCES contacts(PhoneNumber),
    EncryptedPassword CHARACTER VARYING(100)
);