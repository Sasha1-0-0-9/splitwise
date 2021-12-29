CREATE TABLE groups
(
    Id SERIAL PRIMARY KEY,
    CreatorId INT REFERENCES accounts(Id),
    Name CHARACTER VARYING(100)
);

CREATE TABLE account_group_info
(
    Id SERIAL PRIMARY KEY,
    AccountId INT REFERENCES accounts(Id),
    GroupId INT REFERENCES groups(Id)
)