CREATE TABLE user_expenses
(
    Id SERIAL PRIMARY KEY,
    lenderId INT REFERENCES accounts(Id),
    borrowerId INT REFERENCES accounts(Id),
    amount DOUBLE PRECISION,
    time TIMESTAMP,
    currency CHARACTER VARYING(6)
);

CREATE TABLE group_expenses
(
    Id SERIAL PRIMARY KEY,
    lenderId INT REFERENCES accounts(Id),
    groupId INT REFERENCES "groups"(Id),
    amount DOUBLE PRECISION,
    time TIMESTAMP,
    currency CHARACTER VARYING(6)
);