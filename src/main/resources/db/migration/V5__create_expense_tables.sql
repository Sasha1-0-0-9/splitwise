CREATE TABLE user_expenses
(
    Id SERIAL PRIMARY KEY,
    LenderId INT REFERENCES accounts(Id),
    BorrowerId INT REFERENCES accounts(Id),
    Amount DOUBLE PRECISION,
    Time TIMESTAMP,
    Currency CHARACTER VARYING(5)
);

CREATE TABLE group_expenses
(
    Id SERIAL PRIMARY KEY,
    LenderId INT REFERENCES accounts(Id),
    GroupId INT REFERENCES "groups"(Id),
    Amount DOUBLE PRECISION,
    Time TIMESTAMP,
    Currency CHARACTER VARYING(5)
)