DROP TABLE expenses;

CREATE TABLE expenses
(
    Id SERIAL PRIMARY KEY,
    LenderId INT,
    BorrowerId INT,
    Amount DOUBLE PRECISION,
    Time TIMESTAMP,
    Currency VARCHAR,
    expenseType VARCHAR
);