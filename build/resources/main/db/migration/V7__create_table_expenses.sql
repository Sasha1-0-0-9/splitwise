CREATE TABLE expenses
(
    Id SERIAL PRIMARY KEY,
    LenderId INT,
    BorrowerId INT REFERENCES accounts(Id),
    Amount DOUBLE PRECISION,
    Time TIMESTAMP,
    Currency CHARACTER VARYING(5)
);