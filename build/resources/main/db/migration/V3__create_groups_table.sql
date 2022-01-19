CREATE TABLE groups
(
    id SERIAL PRIMARY KEY,
    creator_id INT REFERENCES accounts(id),
    name VARCHAR(100)
);